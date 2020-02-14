package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.HttpClientResultCallback;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.entity.NByteArrayEntity;

import java.net.URI;
import java.util.concurrent.Future;

import static org.apache.http.client.utils.URIUtils.extractHost;

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

        byte[] body = bodyProvider.asByteArray(input, flowContext);

        NByteArrayEntity entity = new NByteArrayEntity(body);

        HttpEntityEnclosingRequestBase request = requestFactory.create();

        request.setURI(uri);

        request.setEntity(entity);

        headerProvider.headers().forEach(request::addHeader);

        return client.execute(HttpAsyncMethods.create(extractHost(uri), request), callback);
    }
}
