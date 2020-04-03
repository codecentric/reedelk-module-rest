package com.reedelk.rest.internal.client.body;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;

class EmptyBodyProvider implements BodyProvider {

    static final BodyProvider INSTANCE = new EmptyBodyProvider();

    private EmptyBodyProvider() {
    }

    @Override
    public BodyResult get(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Publisher<byte[]> getAsStream(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean streamable(Message message) {
        return false;
    }
}
