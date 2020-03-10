package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.components.Components;
import com.reedelk.rest.openapi.info.InfoObject;
import com.reedelk.rest.openapi.paths.Paths;
import org.json.JSONObject;

public class OpenAPI implements Serializable {

    private static final String OPENAPI = "3.0.2";

    private Paths paths;
    private InfoObject info;
    private Components components;

    public Paths getPaths() {
        return paths;
    }

    public void setPaths(Paths paths) {
        this.paths = paths;
    }

    public InfoObject getInfo() {
        return info;
    }

    public void setInfo(InfoObject info) {
        this.info = info;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    @Override
    public JSONObject serialize() {
        JSONObject obj = JsonObjectFactory.newJSONObject();
        obj.put("openapi", OPENAPI);
        if (info != null) obj.put("info", info.serialize());
        if (paths != null) obj.put("paths", paths.serialize());
        if (components != null) obj.put("components", components.serialize());
        return obj;
    }
}
