package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Collapsible
@Component(service = OpenApiObject.class, scope = ServiceScope.PROTOTYPE)
public class OpenApiObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.model.OpenApiObject> {

    // Info Object is required by spec
    @Property("Info")
    private InfoObject info = new InfoObject();

    @Property("Components")
    private ComponentsObject components = new ComponentsObject();

    @Property("Servers")
    @TabGroup("Servers")
    @ListDisplayProperty("url")
    @DialogTitle("Server Configuration")
    private List<ServerObject> servers = new ArrayList<>();

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

    public ComponentsObject getComponents() {
        return components;
    }

    public void setComponents(ComponentsObject components) {
        this.components = components;
    }

    @Override
    public com.reedelk.runtime.openapi.v3.model.OpenApiObject map() {
        com.reedelk.runtime.openapi.v3.model.OpenApiObject mappedOpenApi =
                new com.reedelk.runtime.openapi.v3.model.OpenApiObject();
        if (info != null) mappedOpenApi.setInfo(info.map());
        if (components != null) mappedOpenApi.setComponents(components.map());
        if (servers != null) {
            List<com.reedelk.runtime.openapi.v3.model.ServerObject> mappedServers =
                    servers.stream().map(ServerObject::map).collect(toList());
            mappedOpenApi.setServers(mappedServers);
        }
        return mappedOpenApi;
    }
}
