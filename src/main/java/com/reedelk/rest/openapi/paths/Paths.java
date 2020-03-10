package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class Paths implements Serializable {

    private Map<String, PathItemObject> paths = new TreeMap<>();

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

    public boolean contains(String path) {
        return paths.containsKey(path);
    }

    public PathItemObject get(String path) {
        return paths.get(path);
    }
}
