package com.reedelk.rest.internal.client.body;

import com.reedelk.runtime.api.commons.ScriptUtils;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Attachment;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.reactivestreams.Publisher;

import java.util.Map;

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

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public BodyResult get(Message message, FlowContext flowContext) {
        // For the REST Client with POST method, if the body property has
        // not been defined we use the message.payload() by default.
        Object evaluatedObject = body == null ?
                message.payload() :
                scriptEngine.evaluate(body, flowContext, message).orElse(null);

        // If the evaluated object is null, then the payload must be an empty byte array.
        if (evaluatedObject == null) evaluatedObject = new byte[0];

        // The evaluated payload might be an Attachments map (Multipart) or any other value.
        // For any other value we must convert it to byte array otherwise we keep it as Attachments map (Multipart).
        // The body strategy will take care of building the correct HTTP response for Multipart or not Multipart request.
        if (Attachment.isAttachmentMap(evaluatedObject)) {
            Map<String, Attachment> multipartBody = (Map<String,Attachment>) evaluatedObject;
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
            return message.content().isStream();
        }
        return false;
    }
}
