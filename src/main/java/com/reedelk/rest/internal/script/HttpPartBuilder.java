package com.reedelk.rest.internal.script;

import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeFunction;

@Type(
        global = true,
        description = "The HttpPartBuilder creates new HttpPart objects.")
public class HttpPartBuilder {

    @TypeFunction(signature = "create()",
            example = "HttpPartBuilder.create()",
            description = "Creates a new HttpPart object.")
    public HttpPart create() {
        return new HttpPart();
    }
}
