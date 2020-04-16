package com.reedelk.rest.component;

import com.reedelk.rest.TestComponent;
import com.reedelk.rest.internal.commons.HttpProtocol;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.reedelk.rest.internal.commons.HttpHeader.CONTENT_TYPE;
import static com.reedelk.runtime.api.message.content.MimeType.TEXT_PLAIN;

class RESTClientFollowRedirectsTest extends RESTClientAbstractTest {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldFollowRedirectsByDefault() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());

        RESTClient component = clientWith(RestMethod.GET, configuration, PATH);


        givenThat(any(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withHeader("Location", "/v2/resource")
                        .withStatus(301)));

        givenThat(any(urlEqualTo("/v2/resource"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody("Redirect success")
                        .withStatus(200)));

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(component, payload, flowContext, "Redirect success", TEXT_PLAIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldFollowRedirectsTrue() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setFollowRedirects(true);

        RESTClient component = clientWith(RestMethod.GET, configuration, PATH);


        givenThat(any(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withHeader("Location", "/v2/resource")
                        .withStatus(301)));

        givenThat(any(urlEqualTo("/v2/resource"))
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, TEXT_PLAIN.toString())
                        .withBody("Redirect success")
                        .withStatus(200)));

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(component, payload, flowContext, "Redirect success", TEXT_PLAIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldNotFollowRedirects() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setFollowRedirects(false);

        RESTClient component = clientWith(RestMethod.GET, configuration, PATH);


        givenThat(any(urlEqualTo(PATH))
                .willReturn(aResponse()
                        .withHeader("Location", "/v2/resource")
                        .withStatus(301)));

        Message payload = MessageBuilder.get(TestComponent.class).empty().build();

        // Expect
        AssertHttpResponse.isNotSuccessful(component, payload, flowContext, 301, "Moved Permanently");
    }
}
