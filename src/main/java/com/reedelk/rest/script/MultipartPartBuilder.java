package com.reedelk.rest.script;

import com.reedelk.runtime.api.message.content.*;

public class MultipartPartBuilder {

    private final MultipartMessageBuilder parent;
    private Part.Builder builder;

    MultipartPartBuilder(MultipartMessageBuilder parent, String partName) {
        this.parent = parent;
        this.builder = Part.builder().name(partName);
    }

    public MultipartPartBuilder attribute(String key, String value) {
        this.builder.attribute(key, value);
        return this;
    }

    public MultipartPartBuilder withBinary(byte[] data) {
        ByteArrayContent content = new ByteArrayContent(data, MimeType.APPLICATION_BINARY);
        this.builder.content(content);
        return this;
    }

    public MultipartPartBuilder withBinary(byte[] data, MimeType mimeType) {
        ByteArrayContent content = new ByteArrayContent(data, mimeType);
        this.builder.content(content);
        return this;
    }

    public MultipartPartBuilder withText(String text) {
        StringContent content = new StringContent(text, MimeType.TEXT);
        this.builder.content(content);
        return this;
    }

    public MultipartPartBuilder withText(String text, MimeType mimeType) {
        StringContent content = new StringContent(text, mimeType);
        this.builder.content(content);
        return this;
    }

    public MultipartMessageBuilder and() {
        Part part = this.builder.build();
        return this.parent.add(part);
    }

    public Parts build() {
        return and().build();
    }
}
