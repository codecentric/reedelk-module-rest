package com.reedelk.rest.openapi.components;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Components implements Serializable {

    private Map<String,SchemaObject> schemas = new HashMap<>();

    public void add(String key, SchemaObject schema) {
        this.schemas.put(key, schema);
    }

    @Override
    public JSONObject serialize() {
        JSONObject allSchemas = JsonObjectFactory.newJSONObject();
        if (!schemas.isEmpty()) {
            schemas.forEach((schemaName, schemaObject) ->
                    allSchemas.put(schemaName, new JSONObject(schemaObject.getSchema())));
        }
        JSONObject components = JsonObjectFactory.newJSONObject();
        components.put("schemas", allSchemas);
        return components;
    }
}
