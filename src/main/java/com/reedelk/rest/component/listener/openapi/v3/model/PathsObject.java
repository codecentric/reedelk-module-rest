package com.reedelk.rest.component.listener.openapi.v3.model;

import java.util.Map;
import java.util.TreeMap;

public class PathsObject {

    private Map<String, Map<RestMethod, OperationObject>> paths = new TreeMap<>();

    public Map<String, Map<RestMethod, OperationObject>> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Map<RestMethod, OperationObject>> paths) {
        this.paths = paths;
    }
}
