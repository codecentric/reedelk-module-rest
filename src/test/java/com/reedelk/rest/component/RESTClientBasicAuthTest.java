package com.reedelk.rest.component;

import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.reedelk.rest.internal.commons.HttpProtocol;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.component.client.Authentication;
import com.reedelk.rest.component.client.BasicAuthenticationConfiguration;
import com.reedelk.runtime.api.exception.ConfigurationException;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.reedelk.rest.internal.commons.RestMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RESTClientBasicAuthTest extends RESTClientAbstractTest {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldCorrectlyPerformBasicAuthentication(String method) {
        // Given
        String username = "test123";
        String password = "pass123";
        BasicAuthenticationConfiguration basicAuth = new BasicAuthenticationConfiguration();
        basicAuth.setPassword(password);
        basicAuth.setUsername(username);

        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setAuthentication(Authentication.BASIC);
        configuration.setBasicAuthentication(basicAuth);

        RESTClient component = clientWith(RestMethod.valueOf(method), configuration, PATH);

        givenThat(any(urlEqualTo(PATH))
                .withHeader("Authorization", StringValuePattern.ABSENT)
                .willReturn(aResponse()
                        .withHeader("WWW-Authenticate", "Basic realm=\"test-realm\"")
                        .withStatus(401)));

        givenThat(any(urlEqualTo(PATH))
                .withBasicAuth(username, password)
                .willReturn(aResponse().withStatus(200)));

        Message payload = MessageBuilder.get().empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(component, payload, flowContext);
    }


    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldCorrectlyPerformBasicAuthenticationWithPreemptive(String method) {
        // Given
        String username = "test123";
        String password = "pass123";
        BasicAuthenticationConfiguration basicAuth = new BasicAuthenticationConfiguration();
        basicAuth.setPassword(password);
        basicAuth.setUsername(username);
        basicAuth.setPreemptive(true);

        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setAuthentication(Authentication.BASIC);
        configuration.setBasicAuthentication(basicAuth);

        RESTClient component = clientWith(RestMethod.valueOf(method), configuration, PATH);

        givenThat(any(urlEqualTo(PATH))
                .withBasicAuth(username, password)
                .willReturn(aResponse().withStatus(200)));

        Message payload = MessageBuilder.get().empty().build();

        // Expect
        AssertHttpResponse.isSuccessful(component, payload, flowContext);
    }

    @Test
    void shouldThrowExceptionWhenBasicAuthenticationButNoConfigIsDefined() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost(HOST);
        configuration.setPort(PORT);
        configuration.setProtocol(HttpProtocol.HTTP);
        configuration.setId(UUID.randomUUID().toString());
        configuration.setAuthentication(Authentication.BASIC);

        RESTClient restClient = new RESTClient();
        restClient.setConfiguration(configuration);
        restClient.setMethod(GET);
        restClient.setPath(PATH);
        setScriptEngine(restClient);
        setClientFactory(restClient);

        // Expect
        ConfigurationException thrown = assertThrows(ConfigurationException.class, restClient::initialize);
        assertThat(thrown).hasMessage("RestClientConfiguration (com.reedelk.rest.component.RestClientConfiguration) has a configuration error: Basic Authentication Configuration must be present in the JSON definition when 'authentication' property is 'BASIC'");
    }
}
