package com.reedelk.rest.client.body;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.reactivestreams.Publisher;

class EmptyBodyProvider implements BodyProvider {

    static final BodyProvider INSTANCE = new EmptyBodyProvider();

    private EmptyBodyProvider() {
    }

    @Override
    public BodyResult asByteArray(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Publisher<byte[]> asStream(Message message, FlowContext flowContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean streamable(Message message) {
        return false;
    }
}
