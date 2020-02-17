package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.body.BodyResult;
import com.reedelk.runtime.api.message.content.ByteArrayContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.Parts;
import com.reedelk.runtime.api.message.content.StringContent;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.nio.entity.NByteArrayEntity;

public class HttpEntityBuilder {

    private BodyResult bodyResult;

    private HttpEntityBuilder() {
    }

    public static HttpEntityBuilder get() {
        return new HttpEntityBuilder();
    }

    public HttpEntityBuilder bodyProvider(BodyResult bodyResult) {
        this.bodyResult = bodyResult;
        return this;
    }

    public HttpEntity build() {
        return bodyResult.isMultipart() ? buildMultipart() : buildByteArray();
    }

    private HttpEntity buildByteArray() {
        byte[] body = bodyResult.getDataAsBytes();
        return new NByteArrayEntity(body);
    }

    private HttpEntity buildMultipart() {
        Parts dataAsMultipart = bodyResult.getDataAsMultipart();
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        dataAsMultipart.forEach((partName, part) -> {
            if (part.getContent() instanceof ByteArrayContent) {
                byte[] dataAsBytes = (byte[]) part.getContent().data();
                String filename = part.getAttributes().getOrDefault("filename", null);
                ContentType contentType = ContentType.DEFAULT_BINARY;
                if (part.getContent().mimeType() != null) {
                    MimeType mimeType = part.getContent().mimeType();
                    contentType = ContentType.create(mimeType.toString());
                }
                builder.addBinaryBody(partName, dataAsBytes, contentType, filename);
            } else if (part.getContent() instanceof StringContent) {
                String dataAsString = (String) part.getContent().data();
                ContentType contentType = ContentType.DEFAULT_TEXT;
                if (part.getContent().mimeType() != null) {
                    MimeType mimeType = part.getContent().mimeType();
                    contentType = ContentType.create(mimeType.toString());
                }
                builder.addTextBody(partName, dataAsString, contentType);
            } else {
                throw new IllegalArgumentException("Exception to be thrown");
            }
        });
        return new MultipartFormEntityWrapper(builder.build());
    }
}
