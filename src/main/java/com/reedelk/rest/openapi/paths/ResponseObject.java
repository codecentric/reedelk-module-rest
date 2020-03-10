package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResponseObject implements Serializable {

    private String description;
    private Map<String, MediaTypeObject> content = new HashMap<>();

    public void add(String contentType, MediaTypeObject mediaTypeObject) {
        this.content.put(contentType, mediaTypeObject);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JSONObject serialize() {
        JSONObject responseObject = JsonObjectFactory.newJSONObject();
        responseObject.put("description", description);
        JSONObject all = JsonObjectFactory.newJSONObject();
        responseObject.put("content", all);

        if (!content.isEmpty()) {
            content.forEach((contentType, mediaTypeObject) ->
                    all.put(contentType, mediaTypeObject.serialize()));
        }
        return responseObject;
    }
}
