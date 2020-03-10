package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class MediaTypeObject implements Serializable {

    private ReferenceObject schema;

    public ReferenceObject getSchema() {
        return schema;
    }

    public void setSchema(ReferenceObject schema) {
        this.schema = schema;
    }

    @Override
    public JSONObject serialize() {
        JSONObject mediaType = JsonObjectFactory.newJSONObject();
        mediaType.put("schema", schema.serialize());
        return mediaType;
    }
}
