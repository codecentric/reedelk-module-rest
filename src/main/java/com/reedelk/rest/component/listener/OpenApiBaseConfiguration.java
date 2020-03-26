package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiBaseConfiguration.class, scope = PROTOTYPE)
public class OpenApiBaseConfiguration implements Implementor {

    @Property("Disabled")
    private Boolean disabled;

    @Property("Title")
    private String title;

    @Property("Description")
    private String description;

    @Property("Version")
    private String version;

    @Property("Server")
    @KeyName("Server URL")
    @ValueName("Server Definition")
    private Map<String, OpenApiServerDefinition> servers;

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, OpenApiServerDefinition> getServers() {
        return servers;
    }

    public void setServers(Map<String, OpenApiServerDefinition> servers) {
        this.servers = servers;
    }
}
