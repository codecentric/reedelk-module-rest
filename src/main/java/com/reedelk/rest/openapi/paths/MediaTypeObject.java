package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class MediaTypeObject implements Serializable {

    private ReferenceObject schema;
    private String example;

    public ReferenceObject getSchema() {
        return schema;
    }

    public void setSchema(ReferenceObject schema) {
        this.schema = schema;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    @Override
    public JSONObject serialize() {
        JSONObject mediaType = JsonObjectFactory.newJSONObject();
        if (schema != null) mediaType.put("schema", schema.serialize());
        if (example != null) mediaType.put("example", example);
        return mediaType;
    }
}
