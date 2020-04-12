package com.reedelk.rest.internal.client.uri;

import com.reedelk.rest.component.RestClient1Configuration;
import com.reedelk.runtime.api.exception.ConfigurationException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.ScriptEngineService;
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
        RestClient1Configuration configuration = new RestClient1Configuration();
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
        RestClient1Configuration configuration = new RestClient1Configuration();

        // Expect
        ConfigurationException thrown = assertThrows(ConfigurationException.class,
                () -> UriEvaluator.builder()
                .configuration(configuration)
                .scriptEngine(mockScriptEngine)
                .build());

        assertThat(thrown).isNotNull();
    }

    @Test
    void shouldCorrectlyBuildUriWithBasePath() {
        // Given
        RestClient1Configuration configuration = new RestClient1Configuration();
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
        RestClient1Configuration configuration = new RestClient1Configuration();
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
        ConfigurationException thrown = assertThrows(ConfigurationException.class,
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
