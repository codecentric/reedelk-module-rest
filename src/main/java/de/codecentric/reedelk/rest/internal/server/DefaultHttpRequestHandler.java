package de.codecentric.reedelk.rest.internal.server;

import de.codecentric.reedelk.rest.component.listener.ErrorResponse;
import de.codecentric.reedelk.rest.component.listener.Response;
import de.codecentric.reedelk.rest.internal.ServerTooBusyException;
import de.codecentric.reedelk.rest.internal.commons.StreamingMode;
import de.codecentric.reedelk.rest.internal.server.body.BodyProvider;
import de.codecentric.reedelk.rest.internal.server.body.BodyProviderStreamAlways;
import de.codecentric.reedelk.rest.internal.server.body.BodyProviderStreamAuto;
import de.codecentric.reedelk.rest.internal.server.body.BodyProviderStreamNone;
import de.codecentric.reedelk.rest.internal.server.mapper.HttpRequestMessageMapper;
import de.codecentric.reedelk.rest.internal.server.mapper.MessageHttpResponseMapper;
import de.codecentric.reedelk.runtime.api.commons.StackTraceUtils;
import de.codecentric.reedelk.runtime.api.component.InboundEventListener;
import de.codecentric.reedelk.runtime.api.component.OnResult;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import de.codecentric.reedelk.rest.internal.commons.HttpHeader;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

import static de.codecentric.reedelk.runtime.api.commons.StackTraceUtils.asByteArray;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.SERVICE_UNAVAILABLE;
import static java.lang.String.format;

public class DefaultHttpRequestHandler implements HttpRequestHandler {

    private InboundEventListener inboundEventListener;
    private MessageHttpResponseMapper responseMapper;
    private HttpRequestMessageMapper requestMapper;
    private BodyProvider bodyProvider;

    private DefaultHttpRequestHandler() {
    }

    /**
     * This handler performs the following operations:
     * 1. Maps the incoming HTTP request to a Message
     * 2. Passes down through the processors pipeline the Message
     * 3. Maps back the out Message to the HTTP response
     * 4. Streams back to the HTTP response channel the response data stream
     */
    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {

        Message inputMessage;
        try {
            // Map HTTP request to Message object.
            inputMessage = requestMapper.map(request);

        } catch (Throwable exception) {

            byte[] bodyBytes = asByteArray(exception);

            response.status(INTERNAL_SERVER_ERROR);

            response.addHeader(HttpHeader.CONTENT_LENGTH, String.valueOf(bodyBytes.length));

            return Mono.from(response.sendByteArray(Mono.just(bodyBytes)));
        }

        // Propagate the event down to the processors chain by sending an event.
        return Mono.just(inputMessage)

                // Propagate and map back the out Message as HTTP response.
                // The sink is used to stream out the bytes to be sent to the client.
                .flatMap(message -> Mono.create((Consumer<MonoSink<Publisher<byte[]>>>) bytesSink ->
                        inboundEventListener.onEvent(message, new OnPipelineResult(bytesSink, response))))

                // Streams back to the HTTP response channel the response data stream
                .flatMap(byteStream -> Mono.from(response.sendByteArray(byteStream)));
    }

    /**
     * This class maps back the out Message object to the http response to be sent.
     * It feeds a sink which will be used to stream ta back to the client.
     */
    private class OnPipelineResult implements OnResult {

        private final MonoSink<Publisher<byte[]>> sink;
        private final HttpServerResponse response;

        private OnPipelineResult(MonoSink<Publisher<byte[]>> sink, HttpServerResponse response) {
            this.sink = sink;
            this.response = response;
        }

        @Override
        public void onResult(FlowContext flowContext, Message outMessage) {
            try {

                responseMapper.map(outMessage, response, flowContext);

                Publisher<byte[]> body = bodyProvider.from(response, outMessage, flowContext);

                sink.success(body);

            } catch (Throwable exception) {

                // An exception happened while executing the response mapping.
                // We handle the error before sending it to the client.
                // We must clear up all the response headers since we might have
                // partially added them during the mapping.
                response.responseHeaders().clear();

                handleErrorResponse(exception, flowContext);

            }
        }

        @Override
        public void onError(FlowContext flowContext, Throwable exception) {

            Throwable realException = exception;

            if (exception instanceof RejectedExecutionException) {
                // Server is too  busy, there are not enough Threads able to handle the request.

                String errorMessage = SERVICE_UNAVAILABLE.code() + " Service Temporarily Unavailable (Server is too busy)";

                realException = new ServerTooBusyException(errorMessage, exception);
            }

            handleErrorResponse(realException, flowContext);

        }

        private void handleErrorResponse(Throwable exception, FlowContext flowContext) {

            try {
                responseMapper.map(exception, response, flowContext);

                Publisher<byte[]> body = bodyProvider.from(response, exception, flowContext);

                sink.success(body);

            } catch (Throwable error) {
                // Mapping an error might thrown an exception too! This for instance might
                // happen when the Error response body script contains an error and therefore
                // the 'responseMapper.map' call above might throw an exception as well. In that
                // case we cannot do nothing and return an internal server error to the client.
                response.status(INTERNAL_SERVER_ERROR);

                Publisher<byte[]> body = StackTraceUtils.asByteStream(error);

                sink.success(body);
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String matchingPath;

        private Response response;
        private StreamingMode streaming;
        private ErrorResponse errorResponse;
        private ScriptEngineService scriptEngine;
        private InboundEventListener inboundEventListener;

        public Builder matchingPath(String matchingPath) {
            this.matchingPath = matchingPath;
            return this;
        }

        public Builder response(Response response) {
            this.response = response;
            return this;
        }

        public Builder streaming(StreamingMode streaming) {
            this.streaming = streaming;
            return this;
        }

        public Builder errorResponse(ErrorResponse errorResponse) {
            this.errorResponse = errorResponse;
            return this;
        }

        public Builder scriptEngine(ScriptEngineService scriptEngine) {
            this.scriptEngine = scriptEngine;
            return this;
        }

        public Builder inboundEventListener(InboundEventListener inboundEventListener) {
            this.inboundEventListener = inboundEventListener;
            return this;
        }

        public DefaultHttpRequestHandler build() {
            DefaultHttpRequestHandler handler = new DefaultHttpRequestHandler();
            handler.inboundEventListener = inboundEventListener;
            handler.requestMapper = new HttpRequestMessageMapper(matchingPath);
            handler.responseMapper = new MessageHttpResponseMapper(scriptEngine, response, errorResponse);
            handler.bodyProvider = createBodyProvider();
            return handler;
        }

        private BodyProvider createBodyProvider() {
            DynamicByteArray bodyResponse = response == null ? null : response.getBody();
            DynamicByteArray bodyErrorResponse = errorResponse == null ? null : errorResponse.getBody();
            if (StreamingMode.NONE.equals(streaming)) {
                return new BodyProviderStreamNone(scriptEngine, bodyResponse, bodyErrorResponse);
            } else if (StreamingMode.ALWAYS.equals(streaming)) {
                return new BodyProviderStreamAlways(scriptEngine, bodyResponse, bodyErrorResponse);
            } else if (StreamingMode.AUTO.equals(streaming)) {
                return new BodyProviderStreamAuto(scriptEngine, bodyResponse, bodyErrorResponse);
            } else {
                throw new IllegalArgumentException(format("Execution strategy not available for streaming mode '%s'", streaming));
            }
        }
    }
}
