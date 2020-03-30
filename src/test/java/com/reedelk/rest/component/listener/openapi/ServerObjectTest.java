package com.reedelk.rest.component.listener.openapi;

import com.reedelk.runtime.api.commons.ImmutableMap;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.Map;

import static java.util.Arrays.asList;

class ServerObjectTest {

    @Test
    void shouldCorrectlySerializeServerWithAllProperties() {
        // Given
        ServerObject server = new ServerObject();
        server.setUrl("https://{environment}.{domain}.com/v1");
        server.setDescription("Development server");

        ServerVariableObject environmentVariable = new ServerVariableObject();
        environmentVariable.setEnumValues(asList("dev", "uat", "prod"));
        environmentVariable.setDescription("Environment variable");
        environmentVariable.setDefaultValue("dev");

        ServerVariableObject domainVariable = new ServerVariableObject();
        domainVariable.setEnumValues(asList("localhost", "mydomain1", "mydomain2"));
        domainVariable.setDescription("Domain variable");
        domainVariable.setDefaultValue("localhost");

        Map<String, ServerVariableObject> variables = ImmutableMap.of(
                "environment", environmentVariable,
                "domain", domainVariable);
        server.setVariables(variables);

        // When
        String serialized = server.serialize().toString(2);

        // Then
        String expected = JSONS.ServerObject.WithAllProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }

    @Test
    void shouldCorrectlySerializeServerWithRequiredValues() {
        // Given
        ServerObject server = new ServerObject();

        // When
        String serialized = server.serialize().toString(2);

        // Then
        String expected = JSONS.ServerObject.WithDefaultProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }
}
