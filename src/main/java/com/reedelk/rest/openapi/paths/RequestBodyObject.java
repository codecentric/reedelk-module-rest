package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestBodyObject implements Serializable {

    private boolean required;
    private String description;
    private Map<String, MediaTypeObject> content = new HashMap<>();

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void add(String mediaType, MediaTypeObject mediaTypeObject) {
        this.content.put(mediaType, mediaTypeObject);
    }

    @Override
    public JSONObject serialize() {
        JSONObject requestBody = JsonObjectFactory.newJSONObject();
        requestBody.put("required", required);
        requestBody.put("description", description);
        if (!content.isEmpty()) {
            content.forEach((mediaType, mediaTypeObject) ->
                    requestBody.put(mediaType, mediaTypeObject.serialize()));
        }
        return requestBody;
    }
}
