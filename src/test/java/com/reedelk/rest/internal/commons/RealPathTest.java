package com.reedelk.rest.internal.commons;

import com.reedelk.rest.component.RESTListenerConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RealPathTest {

    @Test
    void shouldReturnCorrectRealPathWhenBasePathIsFrontSlashOnly() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setBasePath("/");

        // When
        String realPath = RealPath.from(configuration, "/test");

        // Then
        assertThat(realPath).isEqualTo("/test");
    }

    @Test
    void shouldReturnCorrectRealPathWhenBasePathIsEmpty() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setBasePath(null);

        // When
        String realPath = RealPath.from(configuration, "/test");

        // Then
        assertThat(realPath).isEqualTo("/test");
    }

    @Test
    void shouldReturnCorrectRealPathWhenBasePathIsNotEmpty() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setBasePath("/api");

        // When
        String realPath = RealPath.from(configuration, "/test");

        // Then
        assertThat(realPath).isEqualTo("/api/test");
    }

    @Test
    void shouldReturnCorrectPathWhenBasePathIsEmptyAndPathIsEmptyAsWell() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();

        // When
        String realPath = RealPath.from(configuration, null);

        // Then
        assertThat(realPath).isEqualTo("/");
    }

    @Test
    void shouldReturnCorrectPathWhenBasePathIsFrontSlashAndPathIsEmpty() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setBasePath("/");

        // When
        String realPath = RealPath.from(configuration, null);

        // Then
        assertThat(realPath).isEqualTo("/");
    }
}
