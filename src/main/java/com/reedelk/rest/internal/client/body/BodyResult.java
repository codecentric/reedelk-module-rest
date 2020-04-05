package com.reedelk.rest.internal.client.body;

import com.reedelk.runtime.api.message.content.Attachment;

import java.util.Map;

public class BodyResult {

    private final boolean isMultipart;
    private final Map<String, Attachment> dataAsMultipart;
    private final byte[] dataAsBytes;

    public BodyResult(Map<String, Attachment> data) {
        this.isMultipart = true;
        this.dataAsMultipart = data;
        this.dataAsBytes = null;
    }

    public BodyResult(byte[] dataAsBytes) {
        this.isMultipart = false;
        this.dataAsMultipart = null;
        this.dataAsBytes = dataAsBytes;
    }

    public boolean isMultipart() {
        return isMultipart;
    }

    public Map<String, Attachment> getDataAsMultipart() {
        return dataAsMultipart;
    }

    public byte[] getDataAsBytes() {
        return dataAsBytes;
    }
}
