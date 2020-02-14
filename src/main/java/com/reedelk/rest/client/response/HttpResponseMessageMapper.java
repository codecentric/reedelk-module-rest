package com.reedelk.rest.client.response;

import com.reedelk.rest.commons.HttpHeadersAsMap;
import com.reedelk.rest.commons.MimeTypeExtract;
import com.reedelk.rest.component.RestClient;
import com.reedelk.runtime.api.commons.JavaType;
import com.reedelk.runtime.api.message.DefaultMessageAttributes;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import org.apache.http.*;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseMessageMapper {

    public static Message map(HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();

        Map<String, Serializable> attributes = new HashMap<>();
        attributes.put(HttpResponseAttribute.STATUS_CODE, statusLine.getStatusCode());
        attributes.put(HttpResponseAttribute.REASON_PHRASE, statusLine.getReasonPhrase());
        attributes.put(HttpResponseAttribute.HEADERS, HttpHeadersAsMap.of(response.getAllHeaders()));

        DefaultMessageAttributes responseAttributes = new DefaultMessageAttributes(RestClient.class, attributes);

        MimeType mimeType = MimeTypeExtract.from(response.getAllHeaders());

        HttpEntity entity = response.getEntity();

        // Empty payload
        if (entity == null) {
            return MessageBuilder.get()
                    .empty()
                    .attributes(responseAttributes)
                    .build();
        }

        // We apply auto-decompression if needed.
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null) {
            HeaderElement[] elements = contentEncoding.getElements();
            // We need to apply decompression in the reverse order they where
            // applied, e.g Content-Encoding: deflate, gzip:
            // in this case first we need to gzip and then deflate.
            for (int i = elements.length - 1; i >= 0; i--) {
                HeaderElement headerElement = elements[i];
                String name = headerElement.getName();
                entity = TYPE_STRATEGY_MAP.getOrDefault(name, DEFAULT_DECOMPRESSING_STRATEGY).apply(entity);
            }
        }

        // Convert the response to string if the mime type is
        // application/json or other string based mime type.
        if (String.class == JavaType.from(mimeType)) {
            String result = EntityUtils.toString(entity);
            return MessageBuilder.get()
                    .attributes(responseAttributes)
                    .withString(result, mimeType)
                    .build();
        } else {
            byte[] bytes = EntityUtils.toByteArray(entity);
            return MessageBuilder.get()
                    .attributes(responseAttributes)
                    .withBinary(bytes, mimeType)
                    .build();
        }
    }

    private static final DecompressingStrategy DEFAULT_DECOMPRESSING_STRATEGY = new DefaultStrategy();
    private static final Map<String, DecompressingStrategy> TYPE_STRATEGY_MAP;

    static {
        Map<String, DecompressingStrategy> tmp = new HashMap<>();
        tmp.put("gzip", new GzipDecompressingEntityStrategy());
        tmp.put("x-gzip", new GzipDecompressingEntityStrategy());
        tmp.put("deflate", new DeflateDecompressingEntityStrategy());
        tmp.put("identity", DEFAULT_DECOMPRESSING_STRATEGY);
        TYPE_STRATEGY_MAP = Collections.unmodifiableMap(tmp);
    }

    interface DecompressingStrategy {
        HttpEntity apply(HttpEntity original);
    }

    static class DefaultStrategy implements DecompressingStrategy {
        @Override
        public HttpEntity apply(HttpEntity original) {
            return original;
        }
    }

    static class GzipDecompressingEntityStrategy implements DecompressingStrategy {
        @Override
        public HttpEntity apply(HttpEntity original) {
            return new GzipDecompressingEntity(original);
        }
    }

    static class DeflateDecompressingEntityStrategy implements DecompressingStrategy {
        @Override
        public HttpEntity apply(HttpEntity original) {
            return new DeflateDecompressingEntity(original);
        }
    }
}
