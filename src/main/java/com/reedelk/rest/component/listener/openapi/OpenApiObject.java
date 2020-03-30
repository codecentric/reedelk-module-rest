package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.List;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiObject.class, scope = PROTOTYPE)
public class OpenApiObject implements Implementor, OpenApiSerializable {

    private static final String OPENAPI = "3.0.2";
    
    // Info Object is required by spec
    @Property("Info")
    private InfoObject info = new InfoObject();

    @Property("Servers")
    @TabGroup("Servers")
    @ListDisplayProperty("url")
    @DialogTitle("Server Configuration")
    private List<ServerObject> servers = new ArrayList<>();

    @Property("Schemas")
    private List<ComponentsObject> components = new ArrayList<>();

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
        JSONObject obj = JsonObjectFactory.newJSONObject();
        obj.put("openapi", OPENAPI);
        obj.put("info", info.serialize());
       // JSONArray serversArray = new JSONArray();
        //for (ServerObject server : servers) {
          //  serversArray.put(server.serialize());
        //}
       // obj.put("servers", serversArray);

        //obj.put("paths", paths.serialize());

        // TODO: Add components.
        return obj;
    }
}
