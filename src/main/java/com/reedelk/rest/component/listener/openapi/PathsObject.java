package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class PathsObject implements OpenApiSerializable {

    private Map<String, OperationObject> paths = new TreeMap<>();

    public void add(String path, OperationObject pathItemObject) {
        if (path == null) {
            paths.put("/", pathItemObject);
        } else {
            paths.put(path, pathItemObject);
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject pathsObject = JsonObjectFactory.newJSONObject();
        paths.forEach((path, pathItemObject) -> pathsObject.put(path, pathItemObject.serialize()));
        return pathsObject;
    }

    public boolean contains(String path) {
        return path == null ?
                paths.containsKey("/") :
                paths.containsKey(path);
    }

    public OperationObject get(String path) {
        return paths.get(path);
    }
}
