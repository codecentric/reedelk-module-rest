package com.reedelk.rest.component.listener.openapi;

import com.reedelk.runtime.api.resource.ResourceText;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static reactor.core.publisher.Mono.just;

class HeaderObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeHeaderWithAllPropertiesAndDefaultSchema() {
        // Given
        HeaderObject header = new HeaderObject();
        header.setAllowReserved(true);
        header.setDeprecated(true);
        header.setExplode(true);
        header.setExample("my header value");
        header.setDescription("My header description");
        header.setStyle(ParameterStyle.spaceDelimited);
        header.setPredefinedSchema(PredefinedSchema.ARRAY_STRING);

        // Expect
        assertSerializedCorrectly(header, OpenApiJsons.HeaderObject.WithAllPropertiesAndDefaultSchema);
    }

    @Test
    void shouldCorrectlySerializeHeaderWithCustomSchema() {
        // Given
        ResourceText schema = mock(ResourceText.class);
        doReturn(just(OpenApiJsons.Schemas.Pet.string())).when(schema).data();
        doReturn("/assets/pet.schema.json").when(schema).path();

        HeaderObject header = new HeaderObject();
        header.setAllowReserved(true);
        header.setDeprecated(true);
        header.setExplode(true);
        header.setExample("my header value");
        header.setDescription("My header description");
        header.setStyle(ParameterStyle.spaceDelimited);
        header.setPredefinedSchema(PredefinedSchema.NONE);
        header.setSchema(schema);

        // Expect
        assertSerializedCorrectly(header, OpenApiJsons.HeaderObject.WithAllPropertiesAndCustomSchema);
    }

    @Test
    void shouldCorrectlySerializeHeaderWithDefaults() {
        // Given
        HeaderObject header = new HeaderObject();

        // Expect
        assertSerializedCorrectly(header, OpenApiJsons.HeaderObject.WithDefaults);
    }
}
