package com.reedelk.rest.component;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.RequestPatternBuilder.newRequestPattern;
import static com.reedelk.rest.internal.commons.HttpHeader.CONTENT_TYPE;
import static com.reedelk.rest.internal.commons.RestMethod.POST;
import static com.reedelk.runtime.api.message.content.MimeType.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;

class RESTClientPostTest extends RESTClientAbstractTest {

    @Nested
    @DisplayName("payload and mime type are correct")
    class PayloadAndContentTypeAreCorrect{

        @Test
        void shouldSetCorrectContentTypeHeaderWhenPayloadIsJson() {
            // Given
            String requestBody = "{\"Name\":\"John\"}";
            byte[] requestBodyAsBytes = requestBody.getBytes();
            String expectedResponseBody = "POST was successful";
            RESTClient component = clientWith(POST, BASE_URL, PATH, EVALUATE_PAYLOAD_BODY);

            doReturn(requestBodyAsBytes)
                    .when(converterService)
                    .convert(requestBodyAsBytes, byte[].class);

            doReturn(Optional.of(requestBody.getBytes()))
                    .when(scriptEngine)
                    .evaluate(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            givenThat(post(urlEqualTo(PATH))
                    .withRequestBody(equalToJson(requestBody))
                    .withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON.toString()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(expectedResponseBody)
                            .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())));


            Message payload = MessageBuilder.get().withJson(requestBody).build();

            // Expect
            AssertHttpResponse.isSuccessful(component, payload, flowContext, expectedResponseBody, TEXT_PLAIN);
        }

        @Test
        void shouldSetCorrectContentTypeHeaderWhenPayloadIsText() {
            // Given
            String requestBody = "text payload";
            byte[] requestBodyAsBytes = requestBody.getBytes();
            String expectedResponseBody = "POST was successful";
            RESTClient component = clientWith(POST, BASE_URL, PATH, EVALUATE_PAYLOAD_BODY);

            doReturn(requestBodyAsBytes)
                    .when(converterService)
                    .convert(requestBodyAsBytes, byte[].class);

            doReturn(Optional.of(requestBody.getBytes()))
                    .when(scriptEngine)
                    .evaluate(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            givenThat(post(urlEqualTo(PATH))
                    .withRequestBody(equalTo(requestBody))
                    .withHeader(CONTENT_TYPE, equalTo(TEXT_PLAIN.toString()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(expectedResponseBody)
                            .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())));

            Message payload = MessageBuilder.get().withText(requestBody).build();

            // Expect
            AssertHttpResponse.isSuccessful(component, payload, flowContext, expectedResponseBody, TEXT_PLAIN);
        }

        @Test
        void shouldSetCorrectContentTypeHeaderWhenPayloadIsBinary() {
            // Given
            byte[] requestBody = "My binary request body".getBytes();
            String expectedResponseBody = "POST was successful";
            RESTClient component = clientWith(POST, BASE_URL, PATH, EVALUATE_PAYLOAD_BODY);

            doReturn(requestBody)
                    .when(converterService)
                    .convert(requestBody, byte[].class);

            doReturn(Optional.of(requestBody))
                    .when(scriptEngine)
                    .evaluate(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

            givenThat(post(urlEqualTo(PATH))
                    .withRequestBody(binaryEqualTo(requestBody))
                    .withHeader(CONTENT_TYPE, equalTo(APPLICATION_BINARY.toString()))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(expectedResponseBody)
                            .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())));

            Message payload = MessageBuilder.get().withBinary(requestBody).build();

            // Expect
            AssertHttpResponse.isSuccessful(component, payload, flowContext, expectedResponseBody, TEXT_PLAIN);
        }

        @Test
        void shouldNotSetContentTypeHeaderWhenPayloadIsEmpty() {
            // Given
            Message emptyPayload = MessageBuilder.get().empty().build();

            doReturn(Optional.empty())
                    .when(scriptEngine)
                    .evaluate(EVALUATE_PAYLOAD_BODY, flowContext, emptyPayload);

            // Expect
            assertEmptyContentTypeAndPayload(EVALUATE_PAYLOAD_BODY, emptyPayload);
        }

        @Test
        void shouldNotSetContentTypeHeaderAndSendEmptyPayloadWhenBodyIsNull() {
            // Given
            DynamicObject body = null;
            Message emptyPayload = MessageBuilder.get().empty().build();



            // Expect
            assertEmptyContentTypeAndPayload(body, emptyPayload);
        }

        @Test
        void shouldNotSetContentTypeHeaderAndSendEmptyPayloadWhenBodyIsEmptyString() {
            // Given
            DynamicObject body = DynamicObject.from(" ", moduleContext);
            Message emptyPayload = MessageBuilder.get().empty().build();

            doReturn(Optional.empty())
                    .when(scriptEngine)
                    .evaluate(body, flowContext, emptyPayload);

            // Expect
            assertEmptyContentTypeAndPayload(body, emptyPayload);
        }

        @Test
        void shouldNotSetContentTypeHeaderAndSendEmptyPayloadWhenBodyIsEmptyScript() {
            // Given
            DynamicObject body = DynamicObject.from("#[]", moduleContext);
            Message emptyPayload = MessageBuilder.get().empty().build();

            doReturn(Optional.empty())
                    .when(scriptEngine)
                    .evaluate(body, flowContext, emptyPayload);

            // Expect
            assertEmptyContentTypeAndPayload(body, emptyPayload);
        }

        @Test
        void shouldNotSetContentTypeHeaderWhenPayloadIsScript() {
            // Given
            DynamicObject body = DynamicObject.from("#['hello this is a script']", moduleContext);
            String expectedResponseBody = "POST was successful";
            byte[] payloadAsBytes = "hello this is a script".getBytes();
            RESTClient component = clientWith(POST, BASE_URL, PATH, body);

            doReturn(payloadAsBytes)
                    .when(converterService)
                    .convert(payloadAsBytes, byte[].class);

            doReturn(Optional.of(payloadAsBytes))
                    .when(scriptEngine)
                    .evaluate(eq(body), any(FlowContext.class), any(Message.class));

            Message message = MessageBuilder.get().withText("my payload").build();

            givenThat(post(urlEqualTo(PATH))
                    .withRequestBody(equalTo("hello this is a script"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(expectedResponseBody)
                            .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())));

            // Expect
            AssertHttpResponse.isSuccessful(component, message, flowContext, expectedResponseBody, TEXT_PLAIN);
            verify(newRequestPattern().withoutHeader(CONTENT_TYPE));
        }

        void assertEmptyContentTypeAndPayload(DynamicObject body, Message message) {
            // Given
            String expectedResponseBody = "It works";
            RESTClient component = clientWith(POST, BASE_URL, PATH, body);

            givenThat(post(urlEqualTo(PATH))
                    .withRequestBody(binaryEqualTo(new byte[0]))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(expectedResponseBody)
                            .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())));

            // Expect
            AssertHttpResponse.isSuccessful(component, message, flowContext, expectedResponseBody, TEXT_PLAIN);
            verify(newRequestPattern().withoutHeader(CONTENT_TYPE));
        }
    }

    @Test
    void shouldPostThrowExceptionWhenResponseNot2xx() {
        // Given
        String expectedErrorMessage = "Error exception caused by XYZ";
        RESTClient component = clientWith(POST, BASE_URL, PATH);

        givenThat(post(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withStatus(507)
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody(expectedErrorMessage)));

        Message emptyPayload = MessageBuilder.get().empty().build();

        // Expect
        AssertHttpResponse.isNotSuccessful(component, emptyPayload, flowContext, expectedErrorMessage);
    }
}
