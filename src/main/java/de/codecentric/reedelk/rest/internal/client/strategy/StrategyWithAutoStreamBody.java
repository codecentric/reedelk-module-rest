package de.codecentric.reedelk.rest.internal.client.strategy;

import de.codecentric.reedelk.rest.internal.client.HttpClient;
import de.codecentric.reedelk.rest.internal.client.HttpClientResultCallback;
import de.codecentric.reedelk.rest.internal.client.body.BodyProvider;
import de.codecentric.reedelk.rest.internal.client.header.HeaderProvider;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import org.apache.http.HttpResponse;

import java.net.URI;
import java.util.concurrent.Future;

public class StrategyWithAutoStreamBody implements Strategy {

    private StrategyWithBody strategyWithBody;
    private StrategyWithStreamBody strategyWithStreamBody;

    StrategyWithAutoStreamBody(RequestWithBodyFactory requestFactory, int requestBufferSize, int responseBufferSize) {
        this.strategyWithBody = new StrategyWithBody(requestFactory, responseBufferSize);
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
