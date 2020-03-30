package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ResponsesObject.class, scope = PROTOTYPE)
public class ResponsesObject implements Implementor, OpenApiSerializable {

    @Property("Description")
    @Hint("A pet to be returned")
    @Description("A short description of the response.")
    private String description;

    @Property("Responses")
    @KeyName("Status Code")
    @ValueName("Edit Content")
    @DialogTitle("Response Content")
    @TabGroup("Tags, Responses and Headers")
    private Map<String, MediaTypeObject> content = new HashMap<>();

    @Property("Headers")
    @KeyName("Header Name")
    @ValueName("Edit Header")
    @DialogTitle("Response Headers")
    @TabGroup("Tags, Responses and Headers")
    private Map<String, HeaderObject> headers = new HashMap<>();


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

    public Map<String, HeaderObject> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, HeaderObject> headers) {
        this.headers = headers;
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        if (!content.isEmpty()) {
        } else {
            // Default response
            JSONObject successfulResponse = JsonObjectFactory.newJSONObject();
            set(successfulResponse, "description", "Successful Response");
            set(serialized, "200", successfulResponse);
        }
        return serialized;
    }
}
