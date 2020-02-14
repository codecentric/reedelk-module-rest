package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.HttpClientResultCallback;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.rest.client.uri.UriProvider;
import com.reedelk.runtime.api.component.OnResult;
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
