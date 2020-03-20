package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.components.Components;
import com.reedelk.rest.openapi.components.SchemaObject;
import com.reedelk.runtime.api.resource.ResourceText;

import java.util.Map;

import static com.reedelk.runtime.api.commons.StreamUtils.FromString;

abstract class AbstractConfigurator implements Configurator {

    protected String schemaRefFrom(OpenAPI api, ResourceText resourceText) {
        // If schema exists
        String schemaPath = resourceText.path();

        Components allComponents = api.getComponents();
        Map<String, SchemaObject> schemas = allComponents.getSchemas();
        SchemaObject first = schemas.values()
                .stream()
                .filter(schemaObject -> {
                    String schemaResourcePath = schemaObject.getSchemaResourcePath();
                    return schemaPath.equals(schemaResourcePath);
                })
                .findFirst()
                .orElseGet(() -> {
                    String jsonSchema = FromString.consume(resourceText.data());
                    String path = resourceText.path();
                    String name = ""; // extract name ->
                    // Remove $schema and stuff
                    // Create and add new schema object.
                    return new SchemaObject(name, path, jsonSchema);
                });

        return first.getRef();
    }
}
