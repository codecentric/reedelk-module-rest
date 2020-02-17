package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.body.BodyResult;
import com.reedelk.rest.server.mapper.MultipartAttribute;
import com.reedelk.runtime.api.message.content.*;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.reedelk.rest.commons.Messages.RestClient.MULTIPART_PART_CONTENT_UNSUPPORTED;
import static com.reedelk.rest.commons.Messages.RestClient.MULTIPART_PART_NULL;

public class HttpEntityBuilder {

    private static final Logger logger = LoggerFactory.getLogger(HttpEntityBuilder.class);

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
        dataAsMultipart.forEach((partName, part) -> buildPart(builder, partName, part));
        return new MultipartFormEntityWrapper(builder.build());
    }

    private ContentType contentTypeOrDefault(TypedContent<?,?> content, ContentType defaultContentType) {
        if (content.mimeType() != null) {
            MimeType mimeType = content.mimeType();
            return ContentType.create(mimeType.toString());
        } else {
            return defaultContentType;
        }
    }

    private void buildPart(MultipartEntityBuilder builder, String partName, Part part) {
        if (part.getContent() instanceof ByteArrayContent) {
            byte[] dataAsBytes = (byte[]) part.getContent().data();
            String filename = part.getAttributes().getOrDefault(MultipartAttribute.FILE_NAME, null);
            ContentType contentType = contentTypeOrDefault(part.getContent(), ContentType.DEFAULT_BINARY);
            builder.addBinaryBody(partName, dataAsBytes, contentType, filename);

        } else if (part.getContent() instanceof StringContent) {
            String dataAsString = (String) part.getContent().data();
            ContentType contentType = contentTypeOrDefault(part.getContent(), ContentType.DEFAULT_TEXT);
            builder.addTextBody(partName, dataAsString, contentType);

        } else if (part.getContent() == null) {
            logger.warn(MULTIPART_PART_NULL.format(partName));

        } else {
            Class<?> contentType = part.getContent().type();
            logger.warn(MULTIPART_PART_CONTENT_UNSUPPORTED.format(contentType.getSimpleName()));
        }
    }
}
