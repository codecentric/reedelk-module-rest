package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.components.Components;
import com.reedelk.rest.openapi.info.InfoObject;
import com.reedelk.rest.openapi.paths.Paths;
import com.reedelk.rest.openapi.server.ServerObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenAPI implements Serializable {

    private static final String OPENAPI = "3.0.2";

    private final Paths paths = new Paths();
    private final InfoObject info = new InfoObject();
    private final List<ServerObject> servers = new ArrayList<>();

    private Components components;

    public Paths getPaths() {
        return paths;
    }

    public InfoObject getInfo() {
        return info;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public List<ServerObject> getServers() {
        return servers;
    }

    @Override
    public JSONObject serialize() {
        JSONObject obj = JsonObjectFactory.newJSONObject();
        obj.put("openapi", OPENAPI);
        obj.put("info", info.serialize());
        JSONArray serversArray = new JSONArray();
        for (ServerObject server : servers) {
            serversArray.put(server.serialize());
        }
        obj.put("servers", serversArray);

        obj.put("paths", paths.serialize());
        if (components != null) obj.put("components", components.serialize());
        return obj;
    }
}
