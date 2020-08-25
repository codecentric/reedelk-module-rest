package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.v3.model.Schema;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Collapsible
@Component(service = ComponentsObject.class, scope = ServiceScope.PROTOTYPE)
public class ComponentsObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.model.ComponentsObject> {

    @Property("Schemas")
    @KeyName("Schema Name")
    @ValueName("Schema Definition")
    @TabGroup("components")
    private Map<String, SchemaObject> schemas = new HashMap<>();

    @Property("Examples")
    @KeyName("Example Name")
    @ValueName("Example Definition")
    @TabGroup("components")
    private Map<String, ExampleComponentObject> examples = new HashMap<>();

    @Property("Security Schemes")
    @KeyName("Security Scheme Name")
    @ValueName("Security Definition")
    @TabGroup("components")
    private Map<String, SecuritySchemeObject> securitySchemes = new HashMap<>();

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    public Map<String, ExampleComponentObject> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, ExampleComponentObject> examples) {
        this.examples = examples;
    }

    public Map<String, SecuritySchemeObject> getSecuritySchemes() {
        return securitySchemes;
    }

    public void setSecuritySchemes(Map<String, SecuritySchemeObject> securitySchemes) {
        this.securitySchemes = securitySchemes;
    }

    @Override
    public com.reedelk.openapi.v3.model.ComponentsObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.model.ComponentsObject mappedComponents =
                new com.reedelk.openapi.v3.model.ComponentsObject();

        // User defined Schemas
        Map<String, com.reedelk.openapi.v3.model.SchemaObject> mappedSchemas = new HashMap<>();
        mappedComponents.setSchemas(mappedSchemas);
        schemas.forEach((schemaId, schemaObject) -> {
            ResourceText schemaResource = schemaObject.getSchema();

            Schema mappedSchema = context.register(schemaId, schemaResource);

            com.reedelk.openapi.v3.model.SchemaObject mappedSchemaObject =
                    new com.reedelk.openapi.v3.model.SchemaObject();
            mappedSchemaObject.setSchema(mappedSchema);
            mappedSchemas.put(schemaId, mappedSchemaObject);
        });

        // User defined Examples
        Map<String, com.reedelk.openapi.v3.model.ExampleObject> mappedExamples = new HashMap<>();
        mappedComponents.setExamples(mappedExamples);
        examples.forEach((exampleId, exampleObject) -> {
            com.reedelk.openapi.v3.model.ExampleObject mappedExample = exampleObject.map(context);
            context.registerExample(exampleId, exampleObject.getValue(), mappedExample);
            mappedExamples.put(exampleId, mappedExample);
        });

        return mappedComponents;
    }
}
