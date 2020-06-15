package com.reedelk.rest.internal.script;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;
import com.reedelk.runtime.api.message.content.Attachment;
import com.reedelk.runtime.api.message.content.MimeType;

@Type(description = "An HttpPart object encapsulates an HTTP part " +
        "to be used in HTTP Multipart requests/responses. The HTTP listener payload contains a map " +
        "of HttpPart objects whenever a POST request has 'multipart/form-data' content type. " +
        "A map of HttpPart objects can be used as an input of a REST Client component to make multipart HTTP requests.")
public class HttpPart {

    private Attachment.Builder current;

    HttpPart() {
        current = Attachment.builder();
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "name(name: String)",
            example = "HttpPartBuilder.create().name('myFile')",
            description = "Sets the name of the attachment.")
    public HttpPart name(String name) {
        current.name(name);
        return this;
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "attribute(key: String, value: String)",
            example = "HttpPartBuilder.create().attribute('filename','my_image.png')",
            description = "Adds a new attribute with the given key and value to the part object.")
    public HttpPart attribute(String key, String value) {
        current.attribute(key, value);
        return this;
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "binary(data: byte[])",
            example = "HttpPartBuilder.create().binary(message.payload())",
            description = "Sets binary data to the current part object. Default mime type is 'application/octet-stream'.")
    public HttpPart binary(byte[] data) {
        current.data(data);
        current.mimeType(MimeType.APPLICATION_BINARY);
        return this;
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "text(data: String)",
            example = "HttpPartBuilder.create().text(message.payload())",
            description = "Sets text data to the current part object. Default mime type is 'text/plain'.")
    public HttpPart text(String text) {
        current.data(text.getBytes());
        current.mimeType(MimeType.TEXT_PLAIN);
        return this;
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "binaryWithMimeType(data: byte[], mimeType: String)",
            example = "HttpPartBuilder.create().binaryWithMimeType(message.payload(), 'application/octet-stream')",
            description = "Sets binary data to the current part object with the given mime type.")
    public HttpPart binaryWithMimeType(byte[] data, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        current.data(data);
        current.mimeType(mimeTypeObject);
        return this;
    }

    @TypeFunction(
            cursorOffset = 1,
            signature = "textWithMimeType(data: String, mimeType: String)",
            example = "HttpPartBuilder.create().textWithMimeType(message.payload(), 'text/plain')",
            description = "Sets text data to the current part object with the given mime type.")
    public HttpPart textWithMimeType(String text, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        current.data(text.getBytes());
        current.mimeType(mimeTypeObject);
        return this;
    }

    @TypeFunction(
            signature = "build()",
            example = "HttpPartBuilder.create().text('sample text').build()",
            description = "Creates an HttpPart object with the configured settings.")
    public Attachment build() {
        return current.build();
    }
}
