package com.reedelk.rest.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.*;

@AutocompleteType(description = "Multipart Part builder")
public class MultipartPartBuilder {

    private final MultipartPartsBuilder parent;
    private Part.Builder current;

    MultipartPartBuilder(MultipartPartsBuilder parent, String partName) {
        this.parent = parent;
        current = Part.builder().name(partName);
    }

    @AutocompleteItem(replaceValue = "attribute('','')", cursorOffset = 5, description = "Adds a new part with the given name")
    public MultipartPartBuilder attribute(String key, String value) {
        current.attribute(key, value);
        return this;
    }

    @AutocompleteItem(replaceValue = "binary()", cursorOffset = 1, description = "Adds a new part with the given name")
    public MultipartPartBuilder binary(byte[] data) {
        ByteArrayContent content = new ByteArrayContent(data, MimeType.APPLICATION_BINARY);
        current.content(content);
        return this;
    }

    @AutocompleteItem(replaceValue = "text('')", cursorOffset = 1, description = "Adds a new part with the given name")
    public MultipartPartBuilder text(String text) {
        StringContent content = new StringContent(text, MimeType.TEXT);
        current.content(content);
        return this;
    }

    @AutocompleteItem(replaceValue = "binaryWithMimeType(,'" + MimeType.MIME_TYPE_APPLICATION_BINARY + "')", cursorOffset = 28, description = "Adds a new part with the given name")
    public MultipartPartBuilder binaryWithMimeType(byte[] data, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        ByteArrayContent content = new ByteArrayContent(data, mimeTypeObject);
        current.content(content);
        return this;
    }

    @AutocompleteItem(replaceValue = "textWithMimeType('','text/plain')", cursorOffset = 15, description = "Adds a new part with the given name")
    public MultipartPartBuilder textWithMimeType(String text, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        StringContent content = new StringContent(text, mimeTypeObject);
        current.content(content);
        return this;
    }

    @AutocompleteItem(replaceValue = "part('')", description = "Adds the current")
    public MultipartPartBuilder part(String partName) {
        parent.add(current.build());
        return parent.part(partName);
    }

    @AutocompleteItem(replaceValue = "build()", description = "Adds a new part with the given name")
    public Parts build() {
        return parent.add(current.build()).build();
    }
}
