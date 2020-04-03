package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.internal.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.resource.ResourceText;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static reactor.core.publisher.Mono.just;

@ExtendWith(MockitoExtension.class)
class ComponentsObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializeSchema() {
        // Given
        ResourceText stringSchema = Mockito.mock(ResourceText.class);
        doReturn(just(PredefinedSchema.STRING.schema())).when(stringSchema).data();
        SchemaObject stringSchemaObject = new SchemaObject();
        stringSchemaObject.setSchema(stringSchema);

        ResourceText integerSchema = Mockito.mock(ResourceText.class);
        doReturn(just(PredefinedSchema.INTEGER.schema())).when(integerSchema).data();
        SchemaObject integerSchemaObject = new SchemaObject();
        integerSchemaObject.setSchema(integerSchema);

        ComponentsObject componentsObject = new ComponentsObject();

        Map<String, SchemaObject> schemas = componentsObject.getSchemas();
        schemas.put("MyString", stringSchemaObject);
        schemas.put("MyInteger", integerSchemaObject);


        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);

        // Expect
        assertSerializedCorrectly(context, componentsObject, OpenApiJsons.ComponentsObject.WithSampleSchemas);
    }

    @Test
    void shouldCorrectlySerializeWhenNoSchemas() {
        // Given
        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);

        // Expect
        assertSerializedCorrectly(context, componentsObject, OpenApiJsons.ComponentsObject.WithNoSchemas);
    }
}
