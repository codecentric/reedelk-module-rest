package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiBaseConfiguration.class, scope = PROTOTYPE)
public class OpenApiBaseConfiguration implements Implementor {

    @Property("Title") // TODO: Required
    @Hint("My API")
    @InitValue("My API")
    @DefaultValue("My API")
    @Description("The title of the API.")
    private String title;

    @Property("Description")
    @Hint("My API description")
    @Description("A short description of the API.")
    private String description;

    @Property("Terms")
    @Hint("http://example.domain.com/terms.html")
    @Description("A URL to the Terms of Service for the API. MUST be in the format of a URL.")
    private String termsOfService;

    @Property("Version")
    @Hint("v1")
    @Example("v1")
    @InitValue("v1")
    @DefaultValue("v1")
    @Description("The version of the OpenAPI document.")
    private String version;

    @Property("Servers")
    @TabGroup("Servers")
    @KeyName("Server URL")
    @ValueName("Server Definition")
    private Map<String, OpenApiServerDefinition> servers = new HashMap<>();

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
