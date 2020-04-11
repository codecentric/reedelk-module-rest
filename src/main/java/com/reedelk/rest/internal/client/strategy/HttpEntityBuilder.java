package com.reedelk.rest.internal.client.strategy;

import com.reedelk.rest.internal.client.body.BodyResult;
import com.reedelk.rest.internal.server.mapper.MultipartAttribute;
import com.reedelk.runtime.api.message.content.*;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static com.reedelk.rest.internal.commons.Messages.RestClient.MULTIPART_PART_CONTENT_UNSUPPORTED;
import static com.reedelk.rest.internal.commons.Messages.RestClient.MULTIPART_PART_NULL;

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
        Map<String, Attachment> dataAsMultipart = bodyResult.getDataAsMultipart();
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

    private void buildPart(MultipartEntityBuilder builder, String partName, Attachment part) {
        TypedContent<?, ?> content = part.getContent();

        if (content instanceof ByteArrayContent) {
            byte[] dataAsBytes = (byte[]) part.getContent().data();

            String filename = part.getAttributes().getOrDefault(MultipartAttribute.FILE_NAME, null);
            ContentType contentType = contentTypeOrDefault(part.getContent(), ContentType.DEFAULT_BINARY);

            ByteArrayBody byteArrayBody = new ByteArrayBody(dataAsBytes, contentType, filename);
            FormBodyPartBuilder formBodyPartBuilder = FormBodyPartBuilder.create(partName, byteArrayBody);
            Optional.ofNullable(part.attributes())
                    .ifPresent(partAttrs -> partAttrs.forEach(formBodyPartBuilder::addField));
            builder.addPart(formBodyPartBuilder.build());

        } else if (content instanceof StringContent) {
            String dataAsString = (String) part.getContent().data();
            ContentType contentType = contentTypeOrDefault(part.getContent(), ContentType.DEFAULT_TEXT);

            StringBody stringBody = new StringBody(dataAsString, contentType);
            FormBodyPartBuilder formBodyPartBuilder = FormBodyPartBuilder.create(partName, stringBody);
            Optional.ofNullable(part.attributes())
                    .ifPresent(partAttrs -> partAttrs.forEach(formBodyPartBuilder::addField));
            builder.addPart(formBodyPartBuilder.build());

        } else if (content == null) {
            logger.warn(MULTIPART_PART_NULL.format(partName));

        } else {
            Class<?> contentType = part.getContent().type();
            logger.warn(MULTIPART_PART_CONTENT_UNSUPPORTED.format(contentType.getSimpleName()));
        }
    }
}
