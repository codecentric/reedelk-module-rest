package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.openapi.OpenApiSerializableContext;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PathsObject extends AbstractOpenApiSerializable {

    private Map<String, Map<RestMethod, OperationObject>> paths = new TreeMap<>();

    @Override
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject pathsObject = JsonObjectFactory.newJSONObject();
        paths.forEach((path, pathItemObject) -> {
            JSONObject operationsByPathJsonObject = JsonObjectFactory.newJSONObject();
            pathItemObject.forEach((restMethod, operationObject) ->
                    operationsByPathJsonObject.put(restMethod.name().toLowerCase(),
                            operationObject.serialize(context)));
            pathsObject.put(path, operationsByPathJsonObject);
        });
        return pathsObject;
    }

    public void add(String path, RestMethod restMethod, OperationObject pathItemObject) {
        Map<RestMethod, OperationObject> operationsByPath = operationsByPathOf(path);
        operationsByPath.put(restMethod, pathItemObject);
    }

    public void add(String path, RestMethod restMethod) {
        Map<RestMethod, OperationObject> operationsByPath = operationsByPathOf(path);
        // Create a default operation object
        OperationObject defaultOperation = new OperationObject();
        operationsByPath.put(restMethod, defaultOperation);
    }

    public void remove(String path, RestMethod restMethod) {
        Map<RestMethod, OperationObject> operationsByPath = operationsByPathOf(path);
        operationsByPath.remove(restMethod);
        if (operationsByPath.isEmpty()) {
            String pathToRemove = realPathOf(path);
            paths.remove(pathToRemove);
        }
    }

    private Map<RestMethod, OperationObject> operationsByPathOf(String path) {
        String fixedPath = realPathOf(path);
        if (!paths.containsKey(fixedPath)) {
            paths.put(fixedPath, new HashMap<>());
        }
        return paths.get(fixedPath);
    }

    private String realPathOf(String path) {
        return path == null ? "/" : path;
    }
}
