package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiRequest.class, scope = PROTOTYPE)
public class OpenApiRequest implements Implementor {

    @Property("Required")
    private Boolean required;

    @Property("Description")
    private String description;

    @Property("Requests")
    @KeyName("Mime Type")
    @ValueName("Request")
    private Map<String, OpenApiRequestDefinition> requests = new HashMap<>();

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, OpenApiRequestDefinition> getRequests() {
        return requests;
    }

    public void setRequests(Map<String, OpenApiRequestDefinition> requests) {
        this.requests = requests;
    }
}
