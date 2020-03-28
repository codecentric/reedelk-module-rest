package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiRequest.class, scope = PROTOTYPE)
public class OpenApiRequest implements Implementor {

    @Property("Required")
    @DefaultValue("false")
    @Description("Determines if the request body is required in the request.")
    private Boolean required;

    @Property("Description")
    @Hint("User to add to the system")
    @Description("A brief description of the request body. This could contain examples of use.")
    private String description;

    @Property("Requests")
    @TabGroup("Tags and Requests")
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
