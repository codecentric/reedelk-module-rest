package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.OpenApiSerializableContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Collapsible
@Component(service = ComponentsObject.class, scope = ServiceScope.PROTOTYPE)
public class ComponentsObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.ComponentsObject> {

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
    public com.reedelk.runtime.openapi.v3.ComponentsObject map(OpenApiSerializableContext context) {
        com.reedelk.runtime.openapi.v3.ComponentsObject mappedComponents =
                new com.reedelk.runtime.openapi.v3.ComponentsObject();

        // Schemas
        Map<String, com.reedelk.runtime.openapi.v3.SchemaObject> mappedSchemas = new HashMap<>();
        schemas.forEach((schemaId, schemaObject) -> mappedSchemas.put(schemaId, schemaObject.map(context)));
        mappedComponents.setSchemas(mappedSchemas);

        return mappedComponents;
    }
}
