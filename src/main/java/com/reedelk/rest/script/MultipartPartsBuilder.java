package com.reedelk.rest.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;
import com.reedelk.runtime.api.message.content.Part;
import com.reedelk.runtime.api.message.content.Parts;

@AutocompleteType(description = "Multipart parts builder")
public class MultipartPartsBuilder {

    private final Parts parts;

    MultipartPartsBuilder() {
        parts = new Parts();
    }

    @AutocompleteItem(signature = "part('')", cursorOffset = 2, description = "Adds a new part with the given name")
    public MultipartPartBuilder part(String partName) {
        return new MultipartPartBuilder(this, partName);
    }

    @AutocompleteItem(signature = "build()", cursorOffset = 1, description = "Adds a new part with the given name")
    public Parts build() {
        return parts;
    }

    public MultipartPartsBuilder add(Part part) {
        this.parts.put(part.getName(), part);
        return this;
    }
}
