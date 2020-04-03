package com.reedelk.rest.internal.client;

import com.reedelk.runtime.api.exception.ESBException;

import java.util.List;
import java.util.Map;

public class HttpClientResponseException extends ESBException {

    private final byte[] data;
    private final int statusCode;
    private final String reasonPhrase;
    private final Map<String, List<String>> headers;

    public HttpClientResponseException(int statusCode, String reasonPhrase, Map<String, List<String>> headers, byte[] data) {
        super();
        this.data = data;
        this.headers = headers;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public synchronized String getMessage() {
        return new String(data);
    }

    // The method get message from an exception might be called
    // multiple times, however, the stream can only be consumed once.
    // That is why we keep a reference of the read message from the stream
    // in order to use it multiple times if needed.

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
