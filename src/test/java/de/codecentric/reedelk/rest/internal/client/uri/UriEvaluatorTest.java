package de.codecentric.reedelk.rest.internal.client.uri;

import de.codecentric.reedelk.rest.component.RESTClientConfiguration;
import de.codecentric.reedelk.runtime.api.exception.ComponentConfigurationException;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UriEvaluatorTest {

    @Mock
    private ScriptEngineService mockScriptEngine;
    @Mock
    private Message message;
    @Mock
    private FlowContext context;

    @Test
    void shouldCorrectlyBuildUriWhenPortIsMissing() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost("localhost");

        UriEvaluator evaluator = UriEvaluator.builder()
                .configuration(configuration)
                .scriptEngine(mockScriptEngine)
                .build();

        UriProvider provider = evaluator.provider(message, context);

        // When
        URI uri = provider.uri();

        assertThat(uri.toString()).isEqualTo("http://localhost");
    }

    @Test
    void shouldThrowExceptionWhenHostIsMissing() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();

        // Expect
        ComponentConfigurationException thrown = assertThrows(ComponentConfigurationException.class,
                () -> UriEvaluator.builder()
                .configuration(configuration)
                .scriptEngine(mockScriptEngine)
                .build());

        assertThat(thrown).isNotNull();
    }

    @Test
    void shouldCorrectlyBuildUriWithBasePath() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost("localhost");
        configuration.setBasePath("/api");

        UriEvaluator evaluator = UriEvaluator.builder()
                .configuration(configuration)
                .scriptEngine(mockScriptEngine)
                .build();

        UriProvider provider = evaluator.provider(message, context);

        // When
        URI uri = provider.uri();

        assertThat(uri.toString()).isEqualTo("http://localhost/api");
    }

    @Test
    void shouldCorrectlyBuildUriWithCustomPortAndBasePath() {
        // Given
        RESTClientConfiguration configuration = new RESTClientConfiguration();
        configuration.setHost("localhost");
        configuration.setPort(8867);
        configuration.setBasePath("/api");

        UriEvaluator evaluator = UriEvaluator.builder()
                .configuration(configuration)
                .scriptEngine(mockScriptEngine)
                .build();

        UriProvider provider = evaluator.provider(message, context);

        // When
        URI uri = provider.uri();

        // Then
        assertThat(uri.toString()).isEqualTo("http://localhost:8867/api");
    }

    @Test
    void shouldThrowExceptionWhenConfigAndBaseURLAreNull() {
        // Given
        ComponentConfigurationException thrown = assertThrows(ComponentConfigurationException.class,
                () -> UriEvaluator.builder()
                        .scriptEngine(mockScriptEngine)
                        .build());

        // Expect
        assertThat(thrown).isNotNull();
    }

    @Test
    void shouldReturnCorrectUrlWhenBaseURLIsPresent() {
        // Given
        UriEvaluator evaluator = UriEvaluator.builder()
                .baseURL("http://localhost/api/resource")
                .scriptEngine(mockScriptEngine)
                .build();

        UriProvider provider = evaluator.provider(message, context);

        // When
        URI uri = provider.uri();

        // Then
        assertThat(uri.toString()).isEqualTo("http://localhost/api/resource");
    }
}
