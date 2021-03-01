package de.codecentric.reedelk.rest.internal.openapi;

import de.codecentric.reedelk.openapi.v3.model.ServerObject;
import de.codecentric.reedelk.openapi.v3.model.ServerVariableObject;
import de.codecentric.reedelk.rest.component.RESTListenerConfiguration;
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

    // The frontend should not be added, because otherwise other import tools,
    // such as Postman will add http://localhost:9292//myApiPath,
    // however we must have http://localhost:9292/myApiPath.
    @Test
    void shouldNotAddFrontSlashWhenEmptyBasePath() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setPort(9292);
        configuration.setHost("0.0.0.0");

        // When
        ServerObject serverObject = DefaultServerObjectBuilder.from(configuration);

        // Then
        String actualUrl = serverObject.getUrl();
        String actualDescription = serverObject.getDescription();
        Map<String, ServerVariableObject> variables = serverObject.getVariables();
        assertThat(actualUrl).isEqualTo("http://0.0.0.0:9292");
        assertThat(actualDescription).isEqualTo("Default Server");
        assertThat(variables).isNull();
    }
}
