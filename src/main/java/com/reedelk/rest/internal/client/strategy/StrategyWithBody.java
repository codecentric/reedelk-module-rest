package com.reedelk.rest.internal.client.strategy;

import com.reedelk.rest.internal.client.HttpClient;
import com.reedelk.rest.internal.client.HttpClientResultCallback;
import com.reedelk.rest.internal.client.body.BodyProvider;
import com.reedelk.rest.internal.client.body.BodyResult;
import com.reedelk.rest.internal.client.header.HeaderProvider;
import com.reedelk.rest.internal.client.response.BufferSizeAwareResponseConsumer;
import com.reedelk.rest.internal.commons.HttpHeader;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import java.net.URI;
import java.util.concurrent.Future;

/**
 * No streaming strategy. Content-Length header is sent.
 */
public class StrategyWithBody implements Strategy {

    private final int responseBufferSize;
    private final RequestWithBodyFactory requestFactory;

    StrategyWithBody(RequestWithBodyFactory requestFactory, int responseBufferSize) {
        this.requestFactory = requestFactory;
        this.responseBufferSize = responseBufferSize;
    }

    @Override
    public Future<HttpResponse> execute(HttpClient client,
                                        Message input,
                                        FlowContext flowContext,
                                        URI uri,
                                        HeaderProvider headerProvider,
                                        BodyProvider bodyProvider,
                                        HttpClientResultCallback callback) {

        BodyResult bodyResult = bodyProvider.get(input, flowContext);

        HttpEntity entity = HttpEntityBuilder.get()
                .bodyProvider(bodyResult)
                .build();

        HttpEntityEnclosingRequestBase request = requestFactory.create();
        request.setURI(uri);
        request.setEntity(entity);

        addHttpHeaders(headerProvider, bodyResult, request);

        HttpAsyncRequestProducer requestProducer = HttpAsyncMethods.create(request);
        HttpAsyncResponseConsumer<HttpResponse> responseConsumer = BufferSizeAwareResponseConsumer.createConsumer(responseBufferSize);
        return client.execute(requestProducer, responseConsumer, callback);
    }

    void addHttpHeaders(HeaderProvider headerProvider, BodyResult bodyResult, HttpEntityEnclosingRequestBase request) {
        headerProvider.headers().forEach((headerName, headerValue) -> {
            if (HttpHeader.CONTENT_TYPE.equalsIgnoreCase(headerName)) {
                // If it is not multipart, we add the content type header,
                // otherwise if it is multipart the content type IS AUTOMATICALLY SET
                // by the Apache Http Client Multipart Entity therefore we don't need to add it.
                if (!bodyResult.isMultipart()) {
                    request.addHeader(headerName, headerValue);
                }
            } else {
                request.addHeader(headerName, headerValue);
            }
        });
    }
}
