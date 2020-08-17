package com.reedelk.rest.internal.openapi;

import com.reedelk.openapi.v3.model.ServerObject;
import com.reedelk.openapi.v3.model.ServerVariableObject;
import com.reedelk.rest.component.RESTListenerConfiguration;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultServerObjectBuilderTest {

    @Test
    void shouldCorrectlyBuildDefaultServerObject() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setPort(9292);
        configuration.setHost("0.0.0.0");
        configuration.setBasePath("/api");

        // When
        ServerObject serverObject = DefaultServerObjectBuilder.from(configuration);

        // Then
        String actualUrl = serverObject.getUrl();
        String actualDescription = serverObject.getDescription();
        Map<String, ServerVariableObject> variables = serverObject.getVariables();
        assertThat(actualUrl).isEqualTo("http://0.0.0.0:9292/api");
        assertThat(actualDescription).isEqualTo("Default Server");
        assertThat(variables).isNull();
    }
}
