package com.reedelk.rest.internal.client.body;

import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Parts;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.reactivestreams.Publisher;

public class DefaultBodyProvider implements BodyProvider {

    private final DynamicObject body;
    private final ConverterService converter;
    private final ScriptEngineService scriptEngine;
    private final boolean isEvaluateMessagePayloadBody;

    DefaultBodyProvider(ScriptEngineService scriptEngine, ConverterService converter, DynamicObject body) {
        this.body = body;
        this.converter =  converter;
        this.scriptEngine = scriptEngine;
        this.isEvaluateMessagePayloadBody = ScriptUtils.isEvaluateMessagePayload(body);
    }

    @Override
    public BodyResult get(Message message, FlowContext flowContext) {
        Object evaluatedObject = scriptEngine.evaluate(body, flowContext, message).orElse(new byte[0]);
        // The evaluated payload might be multipart or any other value.
        // For any other value we must convert it to byte array otherwise we keep it as Multipart.
        // The body strategy will take care of building the correct HTTP response for multipart or not multipart.
        if (evaluatedObject instanceof Parts) {
            Parts multipartBody = (Parts) evaluatedObject;
            return new BodyResult(multipartBody);
        } else {
            byte[] byteArrayBody = converter.convert(evaluatedObject, byte[].class);
            return new BodyResult(byteArrayBody);
        }
    }

    @Override
    public Publisher<byte[]> getAsStream(Message message, FlowContext flowContext) {
        TypedPublisher<Object> objectTypedPublisher = scriptEngine.evaluateStream(body, flowContext, message);
        return converter.convert(objectTypedPublisher, byte[].class);
    }

    @Override
    public boolean streamable(Message message) {
        if (isEvaluateMessagePayloadBody) {
            return message.content().isStream() &&
                    !message.content().isConsumed() &&
                    // Multipart cannot be streamed! However in the listener
                    // the reactor netty API forces us to keep it as a stream and consume
                    // it later, therefore the content with Parts it is a stream however it
                    // cannot be consumed as a stream.
                    !message.content().type().equals(Parts.class);
        }
        return false;
    }
}
