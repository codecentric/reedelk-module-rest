package de.codecentric.reedelk.rest.internal.client.strategy;

import de.codecentric.reedelk.rest.internal.client.body.BodyResult;
import de.codecentric.reedelk.rest.internal.server.mapper.MultipartAttribute;
import de.codecentric.reedelk.runtime.api.message.content.Attachment;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.rest.internal.commons.Messages;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

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

    private ContentType contentTypeOrDefault(MimeType mimeType, ContentType defaultContentType) {
        if (mimeType != null) {
            return ContentType.create(mimeType.toString());
        } else {
            return defaultContentType;
        }
    }

    private void buildPart(MultipartEntityBuilder builder, String partName, Attachment part) {
        byte[] content = part.data();

        if (content != null) {
            String filename = part.attributes().getOrDefault(MultipartAttribute.FILE_NAME, null);
            ContentType contentType = contentTypeOrDefault(part.mimeType(), ContentType.DEFAULT_BINARY);
            ByteArrayBody byteArrayBody = new ByteArrayBody(content, contentType, filename);
            FormBodyPartBuilder formBodyPartBuilder = FormBodyPartBuilder.create(partName, byteArrayBody);
            Optional.ofNullable(part.attributes())
                    .ifPresent(partAttrs -> partAttrs.forEach(formBodyPartBuilder::addField));
            builder.addPart(formBodyPartBuilder.build());

        } else if (content == null) {
            logger.warn(Messages.RestClient.MULTIPART_PART_NULL.format(partName));

        } else {
            logger.warn(Messages.RestClient.MULTIPART_PART_CONTENT_UNSUPPORTED.format(content.getClass().getSimpleName()));
        }
    }
}
