package com.reedelk.rest.internal.client.body;

import com.reedelk.runtime.api.message.content.Attachments;

public class BodyResult {

    private final boolean isMultipart;
    private final Attachments dataAsMultipart;
    private final byte[] dataAsBytes;

    public BodyResult(Attachments data) {
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

    public Attachments getDataAsMultipart() {
        return dataAsMultipart;
    }

    public byte[] getDataAsBytes() {
        return dataAsBytes;
    }
}
