package de.codecentric.reedelk.rest.component;

import com.github.tomakehurst.wiremock.client.WireMock;
import de.codecentric.reedelk.rest.TestComponent;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.rest.internal.commons.HttpHeader;
import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static de.codecentric.reedelk.runtime.api.message.content.MimeType.APPLICATION_JSON;
import static de.codecentric.reedelk.runtime.api.message.content.MimeType.TEXT_PLAIN;

class RESTClientGetTest extends RESTClientAbstractTest {

    @Test
    void shouldGetExecuteCorrectlyWhenResponse200() {
        // Given
        String responseBody = "{\"Name\":\"John\"}";
        RESTClient component = clientWith(RestMethod.GET, BASE_URL, PATH);

        WireMock.givenThat(get(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withHeader(HttpHeader.CONTENT_TYPE, APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBody(responseBody)));

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse
                .isSuccessful(component, payload, flowContext, responseBody, APPLICATION_JSON);
    }

    @Test
    void shouldGetThrowExceptionWhenResponseNot2xxAndNotEmptyBody() {
        // Given
        String expectedErrorMessage = "Error exception caused by XYZ";
        RESTClient component = clientWith(RestMethod.GET, BASE_URL, PATH);

        givenThat(get(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withStatus(507)
                        .withHeader(HttpHeader.CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody(expectedErrorMessage)));

        Message emptyPayload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse
                .isNotSuccessful(component, emptyPayload, flowContext, expectedErrorMessage);
    }

    @Test
    void shouldGetThrowExceptionWhenResponseNot2xxAndEmptyBody() {
        RESTClient component = clientWith(RestMethod.GET, BASE_URL, PATH);

        givenThat(get(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withStatus(500)));

        Message emptyPayload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse
                .isNotSuccessful(component, emptyPayload, flowContext, StringUtils.EMPTY);
    }
}
