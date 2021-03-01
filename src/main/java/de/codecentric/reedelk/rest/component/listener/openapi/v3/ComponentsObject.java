package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.openapi.v3.model.ExampleObject;
import de.codecentric.reedelk.openapi.v3.model.Schema;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Collapsible
@Component(service = ComponentsObject.class, scope = ServiceScope.PROTOTYPE)
public class ComponentsObject implements Implementor, OpenAPIModel<de.codecentric.reedelk.openapi.v3.model.ComponentsObject> {

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
    public de.codecentric.reedelk.openapi.v3.model.ComponentsObject map(OpenApiSerializableContext context) {
        de.codecentric.reedelk.openapi.v3.model.ComponentsObject mappedComponents =
                new de.codecentric.reedelk.openapi.v3.model.ComponentsObject();

        //  Schemas
        Map<String, de.codecentric.reedelk.openapi.v3.model.SchemaObject> mappedSchemas = new HashMap<>();
        mappedComponents.setSchemas(mappedSchemas);
        schemas.forEach((schemaId, schemaObject) -> {
            ResourceText schemaResource = schemaObject.getSchema();

            Schema mappedSchema = context.register(schemaId, schemaResource);

            de.codecentric.reedelk.openapi.v3.model.SchemaObject mappedSchemaObject =
                    new de.codecentric.reedelk.openapi.v3.model.SchemaObject();
            mappedSchemaObject.setSchema(mappedSchema);
            mappedSchemas.put(schemaId, mappedSchemaObject);
        });

        // Examples
        Map<String, de.codecentric.reedelk.openapi.v3.model.ExampleObject> mappedExamples = new HashMap<>();
        mappedComponents.setExamples(mappedExamples);
        examples.forEach((exampleId, exampleObject) -> {
            de.codecentric.reedelk.openapi.v3.model.ExampleObject mappedExample = exampleObject.map(context);
            context.registerExample(exampleId, exampleObject.getValue(), mappedExample);
            mappedExamples.put(exampleId, mappedExample);
        });

        // Security Schemes
        Map<String, de.codecentric.reedelk.openapi.v3.model.SecuritySchemeObject> mappedSecuritySchemes = new HashMap<>();
        mappedComponents.setSecuritySchemes(mappedSecuritySchemes);
        securitySchemes.forEach((securitySchemeId, securitySchemeObject) -> {
            de.codecentric.reedelk.openapi.v3.model.SecuritySchemeObject mappedSecurityScheme =
                    securitySchemeObject.map(context);
            mappedSecuritySchemes.put(securitySchemeId, mappedSecurityScheme);
        });

        return mappedComponents;
    }
}
