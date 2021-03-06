package de.codecentric.reedelk.rest.internal.server.body;

import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerResponse;

public class BodyProviderStreamAuto implements BodyProvider {


    private final boolean isEvaluateMessagePayload;

    private BodyProviderStreamNone streamNone;
    private BodyProviderStreamAlways streamAlways;

    public BodyProviderStreamAuto(ScriptEngineService scriptEngine,
                           DynamicByteArray responseBody,
                           DynamicByteArray errorResponseBody) {
        isEvaluateMessagePayload = ScriptUtils.isEvaluateMessagePayload(responseBody);
        streamNone = new BodyProviderStreamNone(scriptEngine, responseBody, errorResponseBody);
        streamAlways = new BodyProviderStreamAlways(scriptEngine, responseBody, errorResponseBody);
    }

    @Override
    public Publisher<byte[]> from(HttpServerResponse response, Message message, FlowContext flowContext) {
        if (isEvaluateMessagePayload) {
            if (message.content().isStream()) {
                return streamAlways.from(response, message, flowContext);
            }
        }
        return streamNone.from(response, message, flowContext);
    }

    @Override
    public Publisher<byte[]> from(HttpServerResponse response, Throwable throwable, FlowContext flowContext) {
        // An exception is never streamed.
        return streamNone.from(response, throwable, flowContext);
    }
}
