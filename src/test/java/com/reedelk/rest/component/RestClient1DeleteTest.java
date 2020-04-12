package com.reedelk.rest.component;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.reedelk.rest.internal.commons.HttpHeader.CONTENT_TYPE;
import static com.reedelk.rest.internal.commons.RestMethod.DELETE;
import static com.reedelk.runtime.api.message.content.MimeType.TEXT_PLAIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

class RestClient1DeleteTest extends RestClient1AbstractTest {

    @Test
    void shouldDeleteWithBodyExecuteCorrectlyWhenResponse200() {
        // Given
        String requestBody = "{\"Name\":\"John\"}";
        byte[] requestBodyAsBytes = requestBody.getBytes();
        String expectedResponseBody = "DELETE was successful";
        RestClient1 component = clientWith(DELETE, BASE_URL, PATH, EVALUATE_PAYLOAD_BODY);

        doReturn(Optional.of(requestBody.getBytes()))
                .when(scriptEngine)
                .evaluate(eq(EVALUATE_PAYLOAD_BODY), any(FlowContext.class), any(Message.class));

        doReturn(requestBodyAsBytes)
                .when(converterService)
                .convert(requestBodyAsBytes, byte[].class);


        givenThat(delete(urlEqualTo(PATH))
                .withRequestBody(equalToJson(requestBody))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withStatus(200)
                        .withBody(expectedResponseBody)));

        Message payload = MessageBuilder.get().withJson(requestBody).build();

        // Expect
        AssertHttpResponse.isSuccessful(component, payload, flowContext, expectedResponseBody, TEXT_PLAIN);
    }

    @Test
    void shouldDeleteWithEmptyBodyExecuteCorrectlyWhenResponse200() {
        // Given
        String expectedResponseBody = "It works";
        RestClient1 component = clientWith(DELETE, BASE_URL, PATH);

        givenThat(delete(urlEqualTo(PATH))
                .withRequestBody(binaryEqualTo(new byte[0]))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withStatus(200)
                        .withBody(expectedResponseBody)));

        Message emptyPayload = MessageBuilder.get().empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(component, emptyPayload, flowContext, expectedResponseBody, TEXT_PLAIN);
    }

    @Test
    void shouldDeleteThrowExceptionWhenResponseNot2xx() {
        // Given
        String expectedErrorMessage = "Error exception caused by XYZ";
        RestClient1 component = clientWith(DELETE, BASE_URL, PATH);

        givenThat(delete(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withStatus(507)
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody(expectedErrorMessage)));

        Message emptyPayload = MessageBuilder.get().empty().build();

        // Expect
        AssertHttpResponse.isNotSuccessful(component, emptyPayload, flowContext, expectedErrorMessage);
    }
}
