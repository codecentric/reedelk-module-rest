package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.rest.client.uri.UriProvider1;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;

public interface Strategy {

    void execute(HttpClient client,
                 OnResult callback,
                 Message input,
                 FlowContext flowContext,
                 UriProvider1 uriProvider1,
                 HeaderProvider headerProvider,
                 BodyProvider bodyProvider);
}
