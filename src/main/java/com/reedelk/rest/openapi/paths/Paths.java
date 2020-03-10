package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Paths implements Serializable {

    private Map<String, PathItemObject> paths = new HashMap<>();

    public void add(String path, PathItemObject pathItemObject) {
        paths.put(path, pathItemObject);
    }

    @Override
    public JSONObject serialize() {
        JSONObject pathsObject = JsonObjectFactory.newJSONObject();
        paths.forEach((path, pathItemObject) ->
                pathsObject.put(path, pathItemObject.serialize()));
        return pathsObject;
    }
}
