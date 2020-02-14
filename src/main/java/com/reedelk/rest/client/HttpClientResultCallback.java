package com.reedelk.rest.client;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public class HttpClientResultCallback implements FutureCallback<HttpResponse> {

    public HttpClientResultCallback() {
    }

    @Override
    public void completed(HttpResponse result) {

    }

    @Override
    public void failed(Exception exception) {
    }

    @Override
    public void cancelled() {
    }
}
