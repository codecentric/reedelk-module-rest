package com.reedelk.rest.component.listener.openapi;

import com.reedelk.runtime.api.resource.ResourceText;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
public class MediaTypeObjectTest extends AbstractOpenApiSerializableTest {

    @Mock
    private ResourceText schema;
    @Mock
    private ResourceText example;

    @Test
    void shouldCorrectlySerializeMediaTypeWithSchema() {
        // Given
        doReturn(just(OpenApiJsons.Schemas.Pet.string())).when(schema).data();

        MediaTypeObject mediaType = new MediaTypeObject();
        mediaType.setSchema(schema);

        // Expect
        assertSerializedCorrectly(mediaType, OpenApiJsons.MediaTypeObject.WithSchema);
    }

    @Test
    void shouldCorrectlySerializeMediaTypeWithExample() {
        // Given
        doReturn(just(OpenApiJsons.Examples.JsonPet.string())).when(example).data();

        MediaTypeObject mediaType = new MediaTypeObject();
        mediaType.setExample(example);

        // Expect
        assertSerializedCorrectly(mediaType, OpenApiJsons.MediaTypeObject.WithExample);
    }

    @Test
    void shouldCorrectlySerializeMediaTypeWithSchemaAndExample() {
        // Given
        doReturn(just(OpenApiJsons.Examples.JsonPet.string())).when(example).data();
        doReturn(just(OpenApiJsons.Schemas.Pet.string())).when(schema).data();

        MediaTypeObject mediaType = new MediaTypeObject();
        mediaType.setExample(example);
        mediaType.setSchema(schema);

        // Expect
        assertSerializedCorrectly(mediaType, OpenApiJsons.MediaTypeObject.WithSchemaAndExample);
    }

    @Test
    void shouldCorrectlySerializeMediaTypeDefault() {
        // Given
        MediaTypeObject mediaType = new MediaTypeObject();

        // Expect
        assertSerializedCorrectly(mediaType, OpenApiJsons.MediaTypeObject.WithDefault);
    }
}
