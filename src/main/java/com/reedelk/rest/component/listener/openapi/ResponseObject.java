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

// TODO: Required
@Collapsible
@Component(service = ResponseObject.class, scope = PROTOTYPE)
public class ResponseObject implements Implementor, OpenApiSerializable {

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
        /**
        JSONObject responses = JsonObjectFactory.newJSONObject();
        if (!statusCodeAndResponse.isEmpty()) {
            statusCodeAndResponse.forEach((statusCode, responseObject) ->
                    responses.put(statusCode, responseObject.serialize()));
        }
        return responses;*/
        JSONObject responseObject = JsonObjectFactory.newJSONObject();
        responseObject.put("description", description);
        JSONObject all = JsonObjectFactory.newJSONObject();
        responseObject.put("content", all);

        if (!content.isEmpty()) {
            content.forEach((contentType, mediaTypeObject) ->
                    all.put(contentType, mediaTypeObject.serialize()));
        }
        return responseObject;
    }
}
