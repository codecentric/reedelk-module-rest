package com.reedelk.rest.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.*;

import static com.reedelk.runtime.api.autocomplete.AutocompleteItemType.VARIABLE;
import static com.reedelk.runtime.api.commons.Preconditions.checkState;


@AutocompleteType
@AutocompleteItem(token = "MultipartBuilder", itemType = VARIABLE, returnType = MultipartBuilder.class, replaceValue = "MultipartBuilder", description = "Multipart message builder")
public class MultipartBuilder {

    private final Parts parts;
    private Part.Builder current;

    public MultipartBuilder() {
        parts = null;
    }

    private MultipartBuilder(String partName) {
        parts = new Parts();
        add(partName);
    }

    @AutocompleteItem(replaceValue = "create('')", cursorOffset = 2, description = "Creates a new parts builder")
    public MultipartBuilder create(String partName) {
        return new MultipartBuilder(partName);
    }

    @AutocompleteItem(replaceValue = "add('')", cursorOffset = 2, description = "Adds a new part with the given name")
    public MultipartBuilder add(String partName) {
        addCurrent();
        current = Part.builder().name(partName);
        return this;
    }

    @AutocompleteItem(replaceValue = "attribute('','')", cursorOffset = 4, description = "Adds a new part with the given name")
    public MultipartBuilder attribute(String key, String value) {
        current.attribute(key, value);
        return this;
    }

    @AutocompleteItem(replaceValue = "binary()", cursorOffset = 1, description = "Adds a new part with the given name")
    public MultipartBuilder binary(byte[] data) {
        ByteArrayContent content = new ByteArrayContent(data, MimeType.APPLICATION_BINARY);
        current.content(content);
        return this;
    }

    @AutocompleteItem(replaceValue = "text('')", cursorOffset = 1, description = "Adds a new part with the given name")
    public MultipartBuilder text(String text) {
        StringContent content = new StringContent(text, MimeType.TEXT);
        current.content(content);
        return this;
    }

    // TODO: Adjust cursor ofsset
    @AutocompleteItem(replaceValue = "binaryWithMimeType(,'" + MimeType.MIME_TYPE_APPLICATION_BINARY + "')", cursorOffset = 13, description = "Adds a new part with the given name")
    public MultipartBuilder binaryWithMimeType(byte[] data, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        ByteArrayContent content = new ByteArrayContent(data, mimeTypeObject);
        current.content(content);
        return this;
    }

    // TODO: Adjust cursor ofsset
    @AutocompleteItem(replaceValue = "textWithMimeType('','text/plain')", cursorOffset = 5, description = "Adds a new part with the given name")
    public MultipartBuilder textWithMimeType(String text, String mimeType) {
        MimeType mimeTypeObject = MimeType.parse(mimeType);
        StringContent content = new StringContent(text, mimeTypeObject);
        current.content(content);
        return this;
    }

    @AutocompleteItem(replaceValue = "build()", cursorOffset = 1, description = "Adds a new part with the given name")
    public Parts build() {
        checkState(parts != null, "parts");
        addCurrent();
        return parts;
    }

    private void addCurrent() {
        if (current != null) {
            Part part = current.build();
            parts.put(part.getName(), part);
            current = null;
        }
    }
}
