package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;

class OpenApiObjectTest extends AbstractOpenApiSerializableTest {

    /**
     * From https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.3.md#schema
     * An array of Server Objects, which provide connectivity information to a target server.
     * If the servers property is not provided, or is an empty array, the default value would
     * be a Server Object with a url value of /.
     */
    @Test
    void shouldCorrectlySerializeOpenApiWithDefaultServer() {
        // Given
        OpenApiObject openApi = new OpenApiObject();

        // Expect
        assertSerializedCorrectly(openApi, OpenApiJsons.OpenApi.WithDefaultServer);
    }

}
