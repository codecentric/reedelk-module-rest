package com.reedelk.rest.component.listener.openapi;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiBaseConfiguration.class, scope = PROTOTYPE)
public class OpenApiBaseConfiguration implements Implementor {

    @Property("Info")
    private InfoObject info;

    @Property("Servers")
    @TabGroup("Servers")
    @KeyName("Server URL")
    @ValueName("Server Definition")
    private Map<String, ServerObject> servers = new HashMap<>();

    public InfoObject getInfo() {
        return info;
    }

    public void setInfo(InfoObject info) {
        this.info = info;
    }

    public Map<String, ServerObject> getServers() {
        return servers;
    }

    public void setServers(Map<String, ServerObject> servers) {
        this.servers = servers;
    }
}
