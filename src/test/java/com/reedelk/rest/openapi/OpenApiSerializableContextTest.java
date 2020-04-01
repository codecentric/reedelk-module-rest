package com.reedelk.rest.openapi;

import com.reedelk.rest.component.listener.openapi.ComponentsObject;
import com.reedelk.rest.component.listener.openapi.SchemaObject;
import com.reedelk.runtime.api.resource.ResourceText;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static reactor.core.publisher.Mono.*;

@ExtendWith(MockitoExtension.class)
class OpenApiSerializableContextTest {

    private OpenApiSerializableContext context;

    @Mock
    private ResourceText schema;
    private ComponentsObject componentsObject;

    @BeforeEach
    void setUp() {
        componentsObject = new ComponentsObject();
        context = new OpenApiSerializableContext(componentsObject);
    }

    @Test
    void shouldAddSchemaWhenNotPresentInComponentsObject() {
        // Given
        doReturn(just("{}")).when(schema).data();
        doReturn("/assets/car.schema.json").when(schema).path();

        // When
        String schemaReference = context.schemaReferenceOf(schema);

        // Then
        assertThat(schemaReference).isEqualTo("#/components/schemas/car");

        Map<String, SchemaObject> schemas = componentsObject.getSchemas();
        assertThat(schemas).containsOnlyKeys("car");

        SchemaObject schemaObject = schemas.get("car");
        assertThat(schemaObject.getSchema()).isEqualTo(schema);
    }

    @Test
    void shouldNotAddSchemaWhenAlreadyPresentInComponentsObject() {
        // Given
        doReturn("/assets/car.schema.json").when(schema).path();
        SchemaObject schemaObject = new SchemaObject();
        schemaObject.setSchema(schema);
        componentsObject.getSchemas().put("car", schemaObject);

        // When
        String schemaReference = context.schemaReferenceOf(schema);

        // Then
        assertThat(schemaReference).isEqualTo("#/components/schemas/car");
        assertThat(componentsObject.getSchemas()).containsOnlyKeys("car");
    }
}
