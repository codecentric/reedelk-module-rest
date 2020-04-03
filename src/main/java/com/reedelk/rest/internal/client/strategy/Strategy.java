package com.reedelk.rest.internal.client.strategy;

import com.reedelk.rest.internal.client.HttpClient;
import com.reedelk.rest.internal.client.HttpClientResultCallback;
import com.reedelk.rest.internal.client.body.BodyProvider;
import com.reedelk.rest.internal.client.header.HeaderProvider;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
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
