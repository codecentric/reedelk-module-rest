package com.reedelk.rest.client.body;

import com.reedelk.runtime.api.message.content.Parts;

public class BodyResult {

    private final boolean isMultipart;
    private final Parts dataAsMultipart;
    private final byte[] dataAsBytes;

    public BodyResult(Parts data) {
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

    public Parts getDataAsMultipart() {
        return dataAsMultipart;
    }

    public byte[] getDataAsBytes() {
        return dataAsBytes;
    }
}
