package de.codecentric.reedelk.rest.internal.server.body;

import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import de.codecentric.reedelk.rest.internal.commons.HttpHeader;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Optional;

public class BodyProviderStreamNone implements BodyProvider {

    private static final String ZERO_CONTENT_LENGTH = "0";

    private final ScriptEngineService scriptEngine;
    private final DynamicByteArray responseBody;
    private final DynamicByteArray errorResponseBody;

    public BodyProviderStreamNone(ScriptEngineService scriptEngine, DynamicByteArray responseBody, DynamicByteArray errorResponseBody) {
        this.responseBody = responseBody;
        this.scriptEngine = scriptEngine;
        this.errorResponseBody = errorResponseBody;
    }

    // No streaming, single valued Stream (Mono)
    @Override
    public Publisher<byte[]> from(HttpServerResponse response, Message message, FlowContext flowContext) {
        Optional<byte[]> evaluated = scriptEngine.evaluate(responseBody, flowContext, message);
        return publisherFrom(response, evaluated);
    }

    // No streaming, single valued Stream (Mono)
    @Override
    public Publisher<byte[]> from(HttpServerResponse response, Throwable throwable, FlowContext flowContext) {
        try {
            Optional<byte[]> evaluated = scriptEngine.evaluate(errorResponseBody, flowContext, throwable);
            return publisherFrom(response, evaluated);
        } catch (Exception exception) {
            // Evaluating an error response, cannot throw again an exception,
            // Therefore we catch any exception and we return the exception message.
            byte[] exceptionBytes = StackTraceUtils.asByteArray(exception);
            response.addHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(exceptionBytes.length));
            return Mono.just(exceptionBytes);
        }
    }

    private static Publisher<byte[]> publisherFrom(HttpServerResponse response, Optional<byte[]> evaluated) {
        if (evaluated.isPresent()) {
            byte[] responseBody = evaluated.get();
            response.addHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(responseBody.length));
            return Mono.just(responseBody);
        } else {
            response.addHeader(HttpHeader.CONTENT_LENGTH, ZERO_CONTENT_LENGTH);
            return Mono.empty();
        }
    }
}
