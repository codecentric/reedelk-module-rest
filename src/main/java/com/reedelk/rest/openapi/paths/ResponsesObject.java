package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResponsesObject implements Serializable {

    private Map<String, ResponseObject> statusCodeAndResponse = new HashMap<>();

    public void add(String statusCode, ResponseObject responseObject) {
        this.statusCodeAndResponse.put(statusCode, responseObject);
    }

    @Override
    public JSONObject serialize() {
        JSONObject responses = JsonObjectFactory.newJSONObject();
        if (!statusCodeAndResponse.isEmpty()) {
            statusCodeAndResponse.forEach((statusCode, responseObject) ->
                    responses.put(statusCode, responseObject.serialize()));
        }
        return responses;
    }
}
