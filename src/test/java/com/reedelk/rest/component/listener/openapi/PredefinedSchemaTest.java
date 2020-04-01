package com.reedelk.rest.component.listener.openapi;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PredefinedSchemaTest {

    @Test
    void shouldParseStringCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.STRING.schema());

        // Then
        assertThat(object).isNotNull();
    }

    @Test
    void shouldParseArrayStringCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.ARRAY_STRING.schema());

        // Then
        assertThat(object).isNotNull();
    }

    @Test
    void shouldParseIntegerCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.INTEGER.schema());

        // Then
        assertThat(object).isNotNull();
    }

    @Test
    void shouldParseIntegerArrayCorrectly() {
        // Given
        JSONObject object = new JSONObject(PredefinedSchema.ARRAY_INTEGER.schema());

        // Then
        assertThat(object).isNotNull();
    }
}
