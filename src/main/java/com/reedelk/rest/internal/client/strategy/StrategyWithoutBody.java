package com.reedelk.rest.internal.client.strategy;

import com.reedelk.rest.internal.client.HttpClient;
import com.reedelk.rest.internal.client.HttpClientResultCallback;
import com.reedelk.rest.internal.client.body.BodyProvider;
import com.reedelk.rest.internal.client.header.HeaderProvider;
import com.reedelk.rest.internal.client.response.BufferSizeAwareResponseConsumer;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import java.net.URI;
import java.util.concurrent.Future;

/**
 * Strategy for methods without a body: GET, OPTIONS, HEAD
 */
public class StrategyWithoutBody implements Strategy {

    private final int responseBufferSize;
    private final RequestWithoutBodyFactory requestFactory;

    StrategyWithoutBody(RequestWithoutBodyFactory requestFactory, int responseBufferSize) {
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

        HttpRequestBase baseRequest = requestFactory.create();

        baseRequest.setURI(uri);

        headerProvider.headers().forEach(baseRequest::addHeader);

        HttpHost httpHost = URIUtils.extractHost(uri);

        EmptyStreamRequestProducer requestProducer = new EmptyStreamRequestProducer(httpHost, baseRequest);
        HttpAsyncResponseConsumer<HttpResponse> responseConsumer = BufferSizeAwareResponseConsumer.createConsumer(responseBufferSize);

        return client.execute(requestProducer, responseConsumer, callback);
    }
}
