package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static java.util.Arrays.asList;

class ServerVariableObjectTest {

    @Test
    void shouldCorrectlySerializeServerVariableWithAllProperties() {
        // Given
        ServerVariableObject serverVariable = new ServerVariableObject();
        serverVariable.setEnumValues(asList("dev", "uat", "prod"));
        serverVariable.setDescription("Environment variable");
        serverVariable.setDefaultValue("dev");

        // When
        String serialized = serverVariable.serialize().toString(2);

        // Then
        String expected = JSONS.ServerVariableObject.WithAllProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerVariableObject serverVariable = new ServerVariableObject();

        // When
        String serialized = serverVariable.serialize().toString(2);

        // Then
        String expected = JSONS.ServerVariableObject.WithDefaultProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }
}
