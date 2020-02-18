package com.reedelk.rest.script;

import com.reedelk.runtime.api.message.content.Part;
import com.reedelk.runtime.api.message.content.Parts;

public class MultipartMessageBuilder {

    private Parts parts;

    MultipartMessageBuilder() {
         parts = new Parts();
    }

    public MultipartPartBuilder part(String partName) {
        return new MultipartPartBuilder(this, partName);
    }

    MultipartMessageBuilder add(Part part) {
        this.parts.put(part.getName(), part);
        return this;
    }

    public Parts build() {
        return parts;
    }

}
