package de.codecentric.reedelk.rest.internal.client.response;

import de.codecentric.reedelk.rest.component.RESTClient;
import de.codecentric.reedelk.rest.internal.attribute.RESTClientAttributes;
import de.codecentric.reedelk.rest.internal.commons.MimeTypeExtract;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseMessageMapper {

    // 'mapBody' and 'map' should be kept in sync. One it is used when the
    // result of the HTTP Client execution is assigned to a context variable,
    // the other is used when the result is set as a message payload.
    public static Object mapBody(HttpResponse response) throws IOException {
        MimeType mimeType = MimeTypeExtract.from(response.getAllHeaders());
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        entity = applyDecompressStrategyFrom(entity);
        if (String.class == mimeType.javaType()) {
            return EntityUtils.toString(entity);
        } else {
            return EntityUtils.toByteArray(entity);
        }
    }

    public static Message map(HttpResponse response) throws IOException {

        MessageAttributes attributes = new RESTClientAttributes(response);


        MimeType mimeType = MimeTypeExtract.from(response.getAllHeaders());

        HttpEntity entity = response.getEntity();

        // Empty payload
        if (entity == null) {
            return MessageBuilder.get(RESTClient.class)
                    .attributes(attributes)
                    .empty()
                    .build();
        }

        entity = applyDecompressStrategyFrom(entity);

        // Convert the response to string if the mime type is
        // application/json or other string based mime type.
        if (String.class == mimeType.javaType()) {
            String result = EntityUtils.toString(entity);
            return MessageBuilder.get(RESTClient.class)
                    .withString(result, mimeType)
                    .attributes(attributes)
                    .build();
        } else {
            byte[] bytes = EntityUtils.toByteArray(entity);
            return MessageBuilder.get(RESTClient.class)
                    .withBinary(bytes, mimeType)
                    .attributes(attributes)
                    .build();
        }
    }

    public static HttpEntity applyDecompressStrategyFrom(HttpEntity entity) {
        // We apply auto-decompression if needed.
        HttpEntity resultingEntity = entity;
        Header contentEncoding = resultingEntity.getContentEncoding();
        if (contentEncoding != null) {
            HeaderElement[] elements = contentEncoding.getElements();
            // We need to apply decompression in the reverse order they where
            // applied, e.g Content-Encoding: deflate, gzip:
            // in this case first we need to gzip and then deflate.
            for (int i = elements.length - 1; i >= 0; i--) {
                HeaderElement headerElement = elements[i];
                String name = headerElement.getName();
                resultingEntity = TYPE_STRATEGY_MAP.getOrDefault(name, DEFAULT_DECOMPRESSING_STRATEGY).apply(resultingEntity);
            }
        }
        return resultingEntity;
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
