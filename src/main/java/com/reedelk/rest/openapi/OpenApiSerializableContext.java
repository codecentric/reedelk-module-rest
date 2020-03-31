package com.reedelk.rest.openapi;

import com.reedelk.rest.component.listener.openapi.ComponentsObject;
import com.reedelk.rest.component.listener.openapi.SchemaObject;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public class OpenApiSerializableContext {

    private ComponentsObject componentsObject;

    public OpenApiSerializableContext(ComponentsObject componentsObject) {
        this.componentsObject = componentsObject;
    }

    public String schemaReferenceOf(ResourceText schema) {
        if (schema == null) return null; // TODO: Fixme!
        return findSchemaMatching(schema)
                .map(schemaId -> "#/components/schemas/" + schemaId)
                .orElseGet(() -> {
                    Map<String, SchemaObject> schemas = componentsObject.getSchemas();
                    String schemaId = schemaIdFrom(schema);

                    SchemaObject newSchemaObject = new SchemaObject();
                    newSchemaObject.setSchema(schema);
                    schemas.put(schemaId, newSchemaObject);
                    return "#/components/schemas/" + schemaId;
                });
    }

    private String schemaIdFrom(ResourceText schema) {
        String schemaAsJson = StreamUtils.FromString.consume(schema.data());
        JSONObject schemaAsJsonObject = new JSONObject(schemaAsJson);
        String schemaId;
        if (schemaAsJsonObject.has("name")) {
            schemaId = schemaAsJsonObject.getString("name");
        } else {
            String path = schema.path();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            schemaId = idStr.replaceAll("[^a-zA-Z0-9_-]", "");
        }
        return schemaId;
    }

    private Optional<String> findSchemaMatching(ResourceText target) {
        if (target == null) return Optional.empty();

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
