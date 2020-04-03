package com.reedelk.rest.internal.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.Part;
import com.reedelk.runtime.api.message.content.Parts;

@AutocompleteType(description = "Multipart parts builder allows to create multiple parts")
public class MultipartPartsBuilder {

    private final Parts parts;

    MultipartPartsBuilder() {
        parts = new Parts();
    }

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "part(partName: String)",
            example = "MultipartBuilder.part('textContent').text('sample text').part('binaryContent').binary(message.payload()).build()",
            description = "Adds a new part to the Multipart payload with the given partName.")
    public MultipartPartBuilder part(String partName) {
        return new MultipartPartBuilder(this, partName);
    }

    @AutocompleteItem(
            signature = "build()",
            example = "MultipartBuilder.part('textContent').text('sample text').build()",
            description = "Creates the Multipart object with all the configured parts.")
    public Parts build() {
        return parts;
    }

    public MultipartPartsBuilder add(Part part) {
        this.parts.put(part.getName(), part);
        return this;
    }
}
