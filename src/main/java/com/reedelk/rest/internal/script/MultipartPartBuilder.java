package com.reedelk.rest.internal.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.*;

@AutocompleteType(
        description = "The MultipartPartBuilder allows to build a REST Part object. " +
                "The MultipartPartBuilder type is used together with MultipartBuilder to build REST Multipart payloads.")
public class MultipartPartBuilder {

    private final MultipartPartsBuilder parent;
    private Attachment.Builder current;

    MultipartPartBuilder(MultipartPartsBuilder parent, String partName) {
        this.parent = parent;
        current = Attachment.builder().name(partName);
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "attribute(key: String, value: String)",
            example = "MultipartBuilder.part('binaryContent').attribute('filename','my_image.png')",
            description = "Adds a new attribute with the given key and value to the part object.")
    public MultipartPartBuilder attribute(String key, String value) {
        current.attribute(key, value);
        return this;
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "binary(data: byte[])",
            example = "MultipartBuilder.part('binaryContent').binary(message.payload())",
            description = "Sets binary data to the current part object. Default mime type is 'application/octet-stream'.")
    public MultipartPartBuilder binary(byte[] data) {
        ByteArrayContent content = new ByteArrayContent(data, MimeType.APPLICATION_BINARY);
        current.content(content);
        return this;
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "text(data: String)",
            example = "MultipartBuilder.part('textContent').text(message.payload())",
            description = "Sets text data to the current part object. Default mime type is 'text/plain'.")
    public MultipartPartBuilder text(String text) {
        StringContent content = new StringContent(text, MimeType.TEXT_PLAIN);
        current.content(content);
        return this;
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "binaryWithMimeType(data: byte[], mimeType: String)",
            example = "MultipartBuilder.part('binaryContent').binaryWithMimeType(message.payload(), 'application/octet-stream')",
            description = "Sets binary data to the current part object with the given mime type.")
    public MultipartPartBuilder binaryWithMimeType(byte[] data, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        ByteArrayContent content = new ByteArrayContent(data, mimeTypeObject);
        current.content(content);
        return this;
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "textWithMimeType(data: String, mimeType: String)",
            example = "MultipartBuilder.part('textContent').textWithMimeType(message.payload(), 'text/plain')",
            description = "Sets text data to the current part object with the given mime type.")
    public MultipartPartBuilder textWithMimeType(String text, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        StringContent content = new StringContent(text, mimeTypeObject);
        current.content(content);
        return this;
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "part(partName: String)",
            example = "MultipartBuilder.part('textContent').text('sample text').part('binaryContent').binary(message.payload()).build()",
            description = "Adds a new part to the Multipart payload with the given partName.")
    public MultipartPartBuilder part(String partName) {
        parent.add(current.build());
        return parent.part(partName);
    }

    @AutocompleteItem(
            signature = "build()",
            example = "MultipartBuilder.part('textContent').text('sample text').build()",
            description = "Creates the Multipart object with all the configured parts.")
    public Attachments build() {
        return parent.add(current.build()).build();
    }
}
