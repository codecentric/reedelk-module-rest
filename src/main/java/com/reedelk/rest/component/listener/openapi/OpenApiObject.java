package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiObject.class, scope = PROTOTYPE)
public class OpenApiObject implements Implementor, OpenApiSerializable {

    private static final String OPENAPI = "3.0.3";
    
    // Info Object is required by spec
    @Property("Info")
    private InfoObject info = new InfoObject();

    @Property("Components")
    private ComponentsObject components;

    @Property("Servers")
    @TabGroup("Servers")
    @ListDisplayProperty("url")
    @DialogTitle("Server Configuration")
    private List<ServerObject> servers = new ArrayList<>();

    private PathsObject paths = new PathsObject();

    public InfoObject getInfo() {
        return info;
    }

    public void setInfo(InfoObject info) {
        this.info = info;
    }

    public List<ServerObject> getServers() {
        return servers;
    }

    public void setServers(List<ServerObject> servers) {
        this.servers = servers;
    }

    public PathsObject getPaths() {
        return paths;
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        serialized.put("openapi", OPENAPI);
        serialized.put("info", info.serialize());


        if (servers == null || servers.isEmpty()) {
            JSONArray serversArray = new JSONArray();
            serversArray.put(new ServerObject().serialize());
            // From OpenAPI spec 3.0.3:
            // If the servers property is not provided, or is an empty array,
            // the default value would be a Server Object with a url value of /.
            serialized.put("servers", serversArray);
        }

        serialized.put("paths", new JSONObject());

        return serialized;
    }
}