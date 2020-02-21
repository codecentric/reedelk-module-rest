package com.reedelk.rest.component;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.commons.StreamingMode;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedPublisher;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

class RestClientStreamingModeTest extends RestClientAbstractTest {

    @DisplayName("Streaming mode NONE tests")
    @Nested
    class StreamingModeNone {

        @ParameterizedTest
        @ValueSource(strings = {"POST", "PUT", "DELETE"})
        void shouldSendBodyWithCorrectContentLengthHeader(String method) {
            // Given
            RestClientConfiguration configuration = new RestClientConfiguration();
            configuration.setHost(HOST);
            configuration.setPort(PORT);
            configuration.setProtocol(HttpProtocol.HTTP);
            configuration.setId(UUID.randomUUID().toString());

            RestClient component = clientWith(RestMethod.valueOf(method), configuration, PATH, EVALUATE_PAYLOAD_BODY);
            component.setStreaming(StreamingMode.NONE);

            String requestBody = "{\"Name\":\"John\"}";
            byte[] requestBodyAsBytes = requestBody.getBytes();

            doReturn(Optional.of(requestBodyAsBytes))
                    .when(scriptEngine)
                    .evaluate(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            doReturn(requestBodyAsBytes)
                    .when(converterService)
                    .convert(requestBodyAsBytes, byte[].class);

            verify(scriptEngine, never())
                    .evaluateStream(any(DynamicValue.class), any(FlowContext.class), any(Message.class));

            givenThat(WireMock.any(urlEqualTo(PATH))
                    .withHeader("Content-Length", equalTo(String.valueOf(requestBody.getBytes().length)))
                    .willReturn(aResponse()
                            .withStatus(200)));


            Message payload = MessageBuilder.get().withJson(requestBody).build();

            // Expect
            AssertHttpResponse.isSuccessful(component, payload, flowContext);
        }
    }

    @DisplayName("Streaming mode ALWAYS tests")
    @Nested
    class StreamingModeAlways {

        @ParameterizedTest
        @ValueSource(strings = {"POST", "PUT", "DELETE"})
        void shouldSendBodyWithChunkedContent(String method) {
            // Given
            RestClientConfiguration configuration = new RestClientConfiguration();
            configuration.setHost(HOST);
            configuration.setPort(PORT);
            configuration.setProtocol(HttpProtocol.HTTP);
            configuration.setId(UUID.randomUUID().toString());

            RestClient component = clientWith(RestMethod.valueOf(method), configuration, PATH, EVALUATE_PAYLOAD_BODY);
            component.setStreaming(StreamingMode.ALWAYS);

            String requestBodyChunk1 = "{\"Name\":";
            String requestBodyChunk2 = "\"John\"}";

            Flux<byte[]> binaryStream = Flux.just(requestBodyChunk1.getBytes(), requestBodyChunk2.getBytes());
            Message message = MessageBuilder.get().withBinary(binaryStream, MimeType.APPLICATION_JSON).build();

            TypedPublisher<Object> stream = message.content().stream();

            doReturn(stream)
                    .when(scriptEngine)
                    .evaluateStream(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            doReturn(stream)
                    .when(converterService)
                    .convert(stream, byte[].class);

            verify(scriptEngine, never())
                    .evaluate(any(DynamicValue.class), any(FlowContext.class), any(Message.class));

            givenThat(WireMock.any(urlEqualTo(PATH))
                    .withHeader("Transfer-Encoding", equalTo("chunked"))
                    .willReturn(aResponse()
                            .withStatus(200)));


            // Expect
            AssertHttpResponse.isSuccessful(component, message, flowContext);
        }
    }

    @DisplayName("Streaming mode AUTO tests")
    @Nested
    class StreamingModeAuto {

        @ParameterizedTest
        @ValueSource(strings = {"POST", "PUT", "DELETE"})
        void shouldSendBodyWithChunkedContent(String method) {
            // Given
            RestClientConfiguration configuration = new RestClientConfiguration();
            configuration.setHost(HOST);
            configuration.setPort(PORT);
            configuration.setProtocol(HttpProtocol.HTTP);
            configuration.setId(UUID.randomUUID().toString());

            RestClient component = clientWith(RestMethod.valueOf(method), configuration, PATH, EVALUATE_PAYLOAD_BODY);
            component.setStreaming(StreamingMode.AUTO);

            String requestBodyChunk1 = "{\"Name\":";
            String requestBodyChunk2 = "\"John\"}";

            Flux<byte[]> byteArrayStream = Flux.just(requestBodyChunk1.getBytes(), requestBodyChunk2.getBytes());

            Message message = MessageBuilder.get().withBinary(byteArrayStream, MimeType.APPLICATION_JSON).build();

            TypedPublisher<Object> stream = message.content().stream();

            doReturn(stream)
                    .when(scriptEngine)
                    .evaluateStream(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            doReturn(stream)
                    .when(converterService)
                    .convert(stream, byte[].class);

            verify(scriptEngine, never())
                    .evaluate(any(DynamicValue.class), any(FlowContext.class), any(Message.class));

            givenThat(WireMock.any(urlEqualTo(PATH))
                    .withHeader("Transfer-Encoding", equalTo("chunked"))
                    .willReturn(aResponse()
                            .withStatus(200)));

            // Expect
            AssertHttpResponse.isSuccessful(component, message, flowContext);
        }

        @ParameterizedTest
        @ValueSource(strings = {"POST", "PUT", "DELETE"})
        void shouldSendBodyWithContentLengthHeader(String method) {
            // Given
            RestClientConfiguration configuration = new RestClientConfiguration();
            configuration.setHost(HOST);
            configuration.setPort(PORT);
            configuration.setProtocol(HttpProtocol.HTTP);
            configuration.setId(UUID.randomUUID().toString());

            RestClient component = clientWith(RestMethod.valueOf(method), configuration, PATH, EVALUATE_PAYLOAD_BODY);
            component.setStreaming(StreamingMode.AUTO);

            String requestBody = "{\"Name\":\"John\"}";
            byte[] requestBytes = requestBody.getBytes();

            Message payload = MessageBuilder.get().withBinary(requestBytes, MimeType.APPLICATION_JSON).build();

            doReturn(Optional.of(requestBytes))
                    .when(scriptEngine)
                    .evaluate(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            doReturn(requestBytes)
                    .when(converterService)
                    .convert(requestBytes, byte[].class);

            verify(scriptEngine, never())
                    .evaluateStream(any(DynamicValue.class), any(FlowContext.class), any(Message.class));

            givenThat(WireMock.any(urlEqualTo(PATH))
                    .withHeader("Content-Length", equalTo(String.valueOf(requestBody.getBytes().length)))
                    .willReturn(aResponse()
                            .withStatus(200)));

            // Expect
            AssertHttpResponse.isSuccessful(component, payload, flowContext);
        }
    }
}
