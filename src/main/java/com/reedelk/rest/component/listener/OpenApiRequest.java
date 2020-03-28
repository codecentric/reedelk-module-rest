package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.List;
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
    @TabGroup("Tags and Requests")
    @KeyName("Mime Type")
    @ValueName("Request")
    private Map<String, OpenApiRequestDefinition> requests = new HashMap<>();

    @Property("Tags")
    @Hint("Tag name")
    @TabGroup("Tags and Requests")
    private List<String> tags;

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
