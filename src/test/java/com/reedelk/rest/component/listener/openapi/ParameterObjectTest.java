package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParameterObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeParameterWithAllProperties() {
        // Given
        ParameterObject parameter = new ParameterObject();
        parameter.setAllowEmptyValue(true);
        parameter.setAllowReserved(true);
        parameter.setDeprecated(true);
        parameter.setRequired(true);
        parameter.setExplode(true);
        parameter.setName("param1");
        parameter.setIn(ParameterLocation.query);
        parameter.setStyle(ParameterStyle.simple);
        parameter.setDescription("My parameter description");
        parameter.setPredefinedSchema(PredefinedSchema.ARRAY_STRING);

        // Expect
        assertSerializedCorrectly(parameter, OpenApiJsons.ParameterObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeParameterWithDefault() {
        // Given
        ParameterObject parameter = new ParameterObject();

        // Expect
        assertSerializedCorrectly(parameter, OpenApiJsons.ParameterObject.WithDefault);
    }

    @Test
    void shouldSetRequiredTrueWhenParameterInPath() {
        // Given
        ParameterObject parameter = new ParameterObject();
        parameter.setIn(ParameterLocation.path);

        // Expect
        assertSerializedCorrectly(parameter, OpenApiJsons.ParameterObject.WithInPath);
    }
}
