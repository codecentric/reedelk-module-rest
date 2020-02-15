package com.reedelk.rest.client.body;

import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Parts;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import org.reactivestreams.Publisher;

public class DefaultBodyProvider implements BodyProvider {

    private final DynamicByteArray body;
    private final ScriptEngineService scriptEngine;
    private final boolean isEvaluateMessagePayloadBody;

    DefaultBodyProvider(ScriptEngineService scriptEngine, DynamicByteArray body) {
        this.body = body;
        this.scriptEngine = scriptEngine;
        this.isEvaluateMessagePayloadBody = ScriptUtils.isEvaluateMessagePayload(body);
    }

    @Override
    public Parts asParts(Message message, FlowContext flowContext) {
        return message.payload();
    }

    @Override
    public byte[] asByteArray(Message message, FlowContext flowContext) {
        return scriptEngine.evaluate(body, flowContext, message).orElse(new byte[0]);
    }

    @Override
    public Publisher<byte[]> asStream(Message message, FlowContext flowContext) {
        return scriptEngine.evaluateStream(body, flowContext, message);
    }

    @Override
    public boolean streamable(Message message) {
        if (isEvaluateMessagePayloadBody) {
            return message.content().isStream() &&
                    !message.content().isConsumed();
        }
        return false;
    }
}
