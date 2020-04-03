package com.reedelk.rest.internal.script;

import com.reedelk.runtime.api.annotation.AutocompleteItem;
import com.reedelk.runtime.api.annotation.AutocompleteType;


@AutocompleteType(global = true,
        description = "The MultipartBuilder type allows to build REST Multipart messages. " +
                "A Multipart message can be used as a payload to post data to a REST endpoint using the REST Client component.")
public class MultipartBuilder {

    @AutocompleteItem(
            cursorOffset = 1,
            signature = "part(partName: String)",
            example = "MultipartBuilder" +
                    ".part('binaryContent')" +
                    ".binary(message.payload())" +
                    ".attribute('filename','my_file.bin')" +
                    ".part('textContent')" +
                    ".text('This is a multipart test')" +
                    ".build();",
            description = "Creates a new MultipartBuilder object containing one part with the given partName.")
    public MultipartPartBuilder part(String partName) {
        MultipartPartsBuilder parts = new MultipartPartsBuilder();
        return parts.part(partName);
    }
}
