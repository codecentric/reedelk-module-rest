package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.HttpClientResultCallback;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.concurrent.Future;

public class StrategyWithAutoStreamBody implements Strategy {

    private StrategyWithBody strategyWithBody;
    private StrategyWithStreamBody strategyWithStreamBody;

    StrategyWithAutoStreamBody(RequestWithBodyFactory requestFactory, int requestBufferSize, int responseBufferSize, Boolean multipart) {
        this.strategyWithBody = new StrategyWithBody(requestFactory, responseBufferSize, multipart);
        this.strategyWithStreamBody = new StrategyWithStreamBody(requestFactory, requestBufferSize, responseBufferSize);
    }

    @Override
    public Future<HttpResponse> execute(HttpClient client, Message input, FlowContext flowContext, URI uri, HeaderProvider headerProvider, BodyProvider bodyProvider, HttpClientResultCallback callback) {
        if (bodyProvider.streamable(input)) {
            return strategyWithStreamBody
                    .execute(client, input, flowContext, uri, headerProvider, bodyProvider, callback);
        } else {
            return strategyWithBody
                    .execute(client, input, flowContext, uri, headerProvider, bodyProvider, callback);
        }
    }
}
