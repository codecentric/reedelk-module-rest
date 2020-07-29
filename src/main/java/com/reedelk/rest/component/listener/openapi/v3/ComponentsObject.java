package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.v3.Schema;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Collapsible
@Component(service = ComponentsObject.class, scope = ServiceScope.PROTOTYPE)
public class ComponentsObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.ComponentsObject> {

    @Property("Schemas")
    @KeyName("Schema Name")
    @ValueName("Schema Definition")
    @TabGroup("Schemas")
    private Map<String, SchemaObject> schemas = new HashMap<>();

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    @Override
    public com.reedelk.openapi.v3.ComponentsObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.ComponentsObject mappedComponents =
                new com.reedelk.openapi.v3.ComponentsObject();

        // Add user defined schemas
        Map<String, com.reedelk.openapi.v3.SchemaObject> mappedSchemas = new HashMap<>();
        mappedComponents.setSchemas(mappedSchemas);
        schemas.forEach((schemaId, schemaObject) -> {
            ResourceText schemaResource = schemaObject.getSchema();

            Schema mappedSchema = context.register(schemaId, schemaResource);

            com.reedelk.openapi.v3.SchemaObject mappedSchemaObject =
                    new com.reedelk.openapi.v3.SchemaObject();
            mappedSchemaObject.setSchema(mappedSchema);
            mappedSchemas.put(schemaId, mappedSchemaObject);
        });

        return mappedComponents;
    }
}
