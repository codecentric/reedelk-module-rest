package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.components.Components;
import com.reedelk.rest.openapi.components.SchemaObject;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import static com.reedelk.runtime.api.commons.StreamUtils.FromString;

abstract class AbstractConfigurator implements Configurator {

    protected String schemaRefFrom(OpenAPI api, ResourceText resourceText) {
        // If schema exists
        String schemaPath = resourceText.path();

        Components allComponents;
        if (api.getComponents() == null) {
            allComponents = new Components();
            api.setComponents(allComponents);
        } else {
            allComponents = api.getComponents();
        }
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

                    JSONObject object = new JSONObject(jsonSchema);
                    if(object.has("name")) {
                        name = object.getString("name");
                    } else {
                        // Extract name from
                        name = new File(path).getName();
                    }

                    SchemaObject schemaObject = new SchemaObject();
                    schemaObject.setName(name);
                    schemaObject.setSchema(jsonSchema);
                    schemaObject.setSchemaResourcePath(path);
                    allComponents.add(name, schemaObject);
                    return schemaObject;
                });

        return first.getRef();
    }
}
