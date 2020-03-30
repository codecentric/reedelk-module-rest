package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = RequestBodyObject.class, scope = PROTOTYPE)
public class RequestBodyObject implements Implementor, Serializable {

    @Property("Required")
    @DefaultValue("false")
    @Description("Determines if the request body is required in the request.")
    private Boolean required;

    @Property("Description")
    @Hint("User to add to the system")
    @Description("A brief description of the request body. This could contain examples of use.")
    private String description;

    @Property("Requests")
    @KeyName("Mime Type")
    @ValueName("Edit Request")
    @TabGroup("Tags and Requests")
    @DialogTitle("Request Content")
    private Map<String, MediaTypeObject> content = new HashMap<>();


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

    public Map<String, MediaTypeObject> getContent() {
        return content;
    }

    public void setContent(Map<String, MediaTypeObject> content) {
        this.content = content;
    }

    @Override
    public JSONObject serialize() {
        JSONObject requestBody = JsonObjectFactory.newJSONObject();
        requestBody.put("required", required);
        requestBody.put("description", description);
        if (!content.isEmpty()) {
            content.forEach((mediaType, mediaTypeObject) ->
                    requestBody.put(mediaType, mediaTypeObject.serialize()));
        }
        return requestBody;
    }
}
