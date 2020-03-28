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
    private OpenApiInfoDefinition info;

    @Property("Servers")
    @TabGroup("Servers")
    @KeyName("Server URL")
    @ValueName("Server Definition")
    private Map<String, OpenApiServerDefinition> servers = new HashMap<>();

    public OpenApiInfoDefinition getInfo() {
        return info;
    }

    public void setInfo(OpenApiInfoDefinition info) {
        this.info = info;
    }

    public Map<String, OpenApiServerDefinition> getServers() {
        return servers;
    }

    public void setServers(Map<String, OpenApiServerDefinition> servers) {
        this.servers = servers;
    }
}
