package com.reedelk.rest.component;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.reedelk.rest.TestComponent;
import com.reedelk.rest.internal.commons.HttpProtocol;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.RequestPatternBuilder.newRequestPattern;
import static com.reedelk.rest.internal.commons.HttpHeader.CONTENT_TYPE;
import static com.reedelk.runtime.api.message.content.MimeType.TEXT_PLAIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

class RESTClientExpectContinueTest extends RESTClientAbstractTest {

    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "DELETE"})
    void shouldNotAddExpectContinueByDefault(String method) {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());

        DynamicObject dynamicBody = DynamicObject.from("my body", moduleContext);
        byte[] evaluatedPayload = "my body".getBytes();

        doReturn(Optional.of(evaluatedPayload))
                .when(scriptEngine)
                .evaluate(eq(dynamicBody), any(FlowContext.class), any(Message.class));

        doReturn(evaluatedPayload)
                .when(converterService)
                .convert(evaluatedPayload, byte[].class);

        givenThat(WireMock.any(urlEqualTo(PATH))
                .withRequestBody(equalTo("my body"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody("Expect continue success")
                        .withStatus(200)));

        RESTClient restClient = clientWith(RestMethod.valueOf(method), configuration, PATH, dynamicBody);

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(restClient, payload, flowContext, "Expect continue success", TEXT_PLAIN);

        WireMock.verify(0, newRequestPattern().withHeader("Expect", equalTo("100-continue")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "DELETE"})
    void shouldNotAddExpectContinueWhenFalse(String method) {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setExpectContinue(false);

        DynamicObject dynamicBody = DynamicObject.from("my body", moduleContext);

        byte[] evaluatedPayload = "my body".getBytes();

        doReturn(Optional.of(evaluatedPayload))
                .when(scriptEngine)
                .evaluate(eq(dynamicBody), any(FlowContext.class), any(Message.class));

        doReturn(evaluatedPayload)
                .when(converterService)
                .convert(evaluatedPayload, byte[].class);

        givenThat(WireMock.any(urlEqualTo(PATH))
                .withRequestBody(equalTo("my body"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody("Expect continue success")
                        .withStatus(200)));

        RESTClient restClient = clientWith(RestMethod.valueOf(method), configuration, PATH, dynamicBody);

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(restClient, payload, flowContext, "Expect continue success", TEXT_PLAIN);

        WireMock.verify(0, newRequestPattern().withHeader("Expect", equalTo("100-continue")));
    }


    @ParameterizedTest
    @ValueSource(strings = {"POST", "PUT", "DELETE"})
    void shouldAddExpectContinueWhenTrue(String method) {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setExpectContinue(true);

        DynamicObject dynamicBody = DynamicObject.from("my body", moduleContext);

        byte[] evaluatedPayload = "my body".getBytes();

        doReturn(Optional.of(evaluatedPayload))
                .when(scriptEngine)
                .evaluate(eq(dynamicBody), any(FlowContext.class), any(Message.class));

        doReturn(evaluatedPayload)
                .when(converterService)
                .convert(evaluatedPayload, byte[].class);

        givenThat(WireMock.any(urlEqualTo(PATH))
                .withRequestBody(equalTo("my body"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody("Expect continue success")
                        .withStatus(200)));

        RESTClient restClient = clientWith(RestMethod.valueOf(method), configuration, PATH, dynamicBody);

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();


        // Expect
        AssertHttpResponse.isSuccessful(restClient, payload, flowContext, "Expect continue success", TEXT_PLAIN);

        WireMock.verify(1, newRequestPattern().withHeader("Expect", equalTo("100-continue")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET","HEAD","OPTIONS"})
    void shouldNotAddExpectContinueWhenTrueToNotEntityEnclosedMethods(String method) {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setExpectContinue(true);

        RESTClient component = clientWith(RestMethod.valueOf(method), configuration, PATH);

        givenThat(WireMock.any(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withBody("Expect continue success")
                        .withStatus(200)));

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(component, payload, flowContext);

        WireMock.verify(0, newRequestPattern().withHeader("Expect", equalTo("100-continue")));
    }
}
