package com.reedelk.rest.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;


@AutocompleteType(global = true, description = "Multipart message builder")
public class MultipartBuilder {

    @AutocompleteItem(replaceValue = "part('')", cursorOffset = 2, description = "Creates a new parts builder")
    public MultipartPartBuilder part(String partName) {
        MultipartPartsBuilder parts = new MultipartPartsBuilder();
        return parts.part(partName);
    }
}
