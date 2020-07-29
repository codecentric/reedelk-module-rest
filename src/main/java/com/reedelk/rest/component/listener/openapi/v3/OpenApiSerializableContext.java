package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.v3.Schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OpenApiSerializableContext {

    private final Map<String, Schema> SCHEMAS_MAP = new HashMap<>();

    public void setSchema(String schemaId, Map<String,Object> schemaData) {
        // If it is a reference we need to register, otherwise we
        // just serialize the schema as is.
        if (!SCHEMAS_MAP.containsKey(schemaId)) {
            SCHEMAS_MAP.put(schemaId, new Schema(schemaData));
        }
    }

    public Map<String, Schema> getSchemas() {
        return Collections.unmodifiableMap(SCHEMAS_MAP);
    }
}
