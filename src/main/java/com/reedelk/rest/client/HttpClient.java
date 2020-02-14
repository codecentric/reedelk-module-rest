package com.reedelk.rest.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

import static java.util.Objects.requireNonNull;

public class HttpClient {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private final CloseableHttpAsyncClient delegate;
    private final HttpClientContextProvider contextProvider;


    HttpClient(CloseableHttpAsyncClient delegate) {
        this.delegate = requireNonNull(delegate, "delegate http client");
        this.contextProvider = null;
    }

    HttpClient(CloseableHttpAsyncClient delegate, HttpClientContextProvider contextProvider) {
        this.delegate = delegate;
        this.contextProvider = contextProvider;
    }

    public Future<HttpResponse> execute(HttpAsyncRequestProducer requestProducer, HttpClientResultCallback callback) {
        HttpAsyncResponseConsumer<HttpResponse> responseConsumer = HttpAsyncMethods.createConsumer();
        if (contextProvider != null) {
            HttpClientContext context = contextProvider.provide();
            return delegate.execute(requestProducer, responseConsumer, context, callback);
        } else {
            return delegate.execute(requestProducer, responseConsumer, callback);
        }
    }

    public void close() {
        try {
            delegate.close();
        } catch (Exception e) {
            logger.warn("Error while closing http client", e);
        }
    }

    public void start() {
        this.delegate.start();
    }
}
