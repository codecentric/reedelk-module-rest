package com.reedelk.rest.internal.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;

@AutocompleteType(
        global = true,
        description = "The HttpPartBuilder creates new HttpPart objects.")
public class HttpPartBuilder {

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "create()",
            example = "HttpPartBuilder.create()",
            description = "Creates a new HttpPart object.")
    public HttpPart create() {
        return new HttpPart();
    }
}
