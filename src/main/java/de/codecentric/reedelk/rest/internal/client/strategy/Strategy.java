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

public interface Strategy {

    Future<HttpResponse> execute(HttpClient client,
                                 Message input,
                                 FlowContext flowContext,
                                 URI uri,
                                 HeaderProvider headerProvider,
                                 BodyProvider bodyProvider,
                                 HttpClientResultCallback callback);
}
