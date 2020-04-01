package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;

class HeaderObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeHeaderWithAllPropertiesAndDefaultSchema() {
        // Given
        HeaderObject header = new HeaderObject();
        header.setAllowEmptyValue(true);
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
}
