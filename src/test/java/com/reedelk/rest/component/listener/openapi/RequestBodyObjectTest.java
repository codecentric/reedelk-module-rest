package com.reedelk.rest.component.listener.openapi;

import com.reedelk.runtime.api.resource.ResourceText;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static reactor.core.publisher.Mono.just;

class RequestBodyObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeRequestBodyWithAllProperties() {
        // Given
        ResourceText example = mock(ResourceText.class);
        doReturn(just(OpenApiJsons.Examples.Pet.string())).when(example).data();

        MediaTypeObject mediaTypeObject = new MediaTypeObject();
        mediaTypeObject.setExample(example);
        Map<String, MediaTypeObject> content = new HashMap<>();
        content.put("application/json", mediaTypeObject);

        RequestBodyObject requestBody = new RequestBodyObject();
        requestBody.setDescription("My request body");
        requestBody.setRequired(true);
        requestBody.setContent(content);

        // Expect
        assertSerializedCorrectly(requestBody, OpenApiJsons.RequestBodyObject.WithAllProperties);
    }

    @Test
    void shouldCorrectlySerializeRequestBodyWithDefaults() {
        // Given
        RequestBodyObject requestBody = new RequestBodyObject();

        // Expect
        assertSerializedCorrectly(requestBody, OpenApiJsons.RequestBodyObject.WithDefault);
    }
}
