package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class ReferenceObject implements Serializable {

    private String $ref;

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = JsonObjectFactory.newJSONObject();
        object.put("$ref", $ref);
        return object;
    }
}
