package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.SchemaId;
import com.reedelk.rest.component.listener.openapi.ComponentsObject;
import com.reedelk.rest.component.listener.openapi.SchemaObject;
import com.reedelk.runtime.api.resource.ResourceText;

import java.util.Map;
import java.util.Optional;

import static com.reedelk.runtime.api.commons.Preconditions.checkNotNull;

public class OpenApiSerializableContext {

    private ComponentsObject componentsObject;

    public OpenApiSerializableContext(ComponentsObject componentsObject) {
        this.componentsObject = componentsObject;
    }

    public String schemaReferenceOf(ResourceText schema) {
        checkNotNull(schema, "schema");

        return findSchemaMatching(schema)
                .map(schemaId -> "#/components/schemas/" + schemaId)
                .orElseGet(() -> {
                    Map<String, SchemaObject> schemas = componentsObject.getSchemas();
                    String schemaId = SchemaId.from(schema);

                    SchemaObject newSchemaObject = new SchemaObject();
                    newSchemaObject.setSchema(schema);
                    schemas.put(schemaId, newSchemaObject);
                    return "#/components/schemas/" + schemaId;
                });
    }

    private Optional<String> findSchemaMatching(ResourceText target) {
        Map<String, SchemaObject> schemas = componentsObject.getSchemas();
        for (Map.Entry<String, SchemaObject> entry : schemas.entrySet()) {
            String currentSchemaId = entry.getKey();
            SchemaObject currentSchemaObject = entry.getValue();
            String path = currentSchemaObject.getSchema().path();
            if (path.equals(target.path())) {
                return Optional.of(currentSchemaId);
            }
        }
        return Optional.empty();
    }
}
