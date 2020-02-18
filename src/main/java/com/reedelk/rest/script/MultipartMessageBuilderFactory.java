package com.reedelk.rest.script;

public class MultipartMessageBuilderFactory {

    public MultipartPartBuilder part(String partName) {
        MultipartMessageBuilder builder = new MultipartMessageBuilder();
        return builder.part(partName);
    }
}
