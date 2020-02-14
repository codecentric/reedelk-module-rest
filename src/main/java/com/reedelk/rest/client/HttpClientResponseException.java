package com.reedelk.rest.client;

import com.reedelk.runtime.api.exception.ESBException;

public class HttpClientResponseException extends ESBException {

    private final byte[] data;
    private final int statusCode;
    private final String reasonPhrase;

    public HttpClientResponseException(int statusCode, String reasonPhrase, byte[] data) {
        super();
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.data = data;
    }

    // The method get message from an exception might be called
    // multiple times, however, the stream can only be consumed once.
    // That is why we keep a reference of the read message from the stream
    // in order to use it multiple times if needed.
    @Override
    public synchronized String getMessage() {
        return new String(data);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
