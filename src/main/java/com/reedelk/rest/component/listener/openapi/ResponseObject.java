package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.internal.commons.JsonObjectFactory;
import com.reedelk.rest.internal.commons.Messages;
import com.reedelk.rest.internal.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.internal.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ResponseObject.class, scope = PROTOTYPE)
public class ResponseObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Description")
    @Hint("Successful Response")
    @Description("A short description of the response.")
    private String description = Messages.RestListener.OPEN_API_SUCCESS_RESPONSE.format();

    @Property("Content")
    @KeyName("Media Type")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "description", description);
        set(serialized, "content", content, context);
        set(serialized, "headers", headers, context);
        return serialized;
    }
}
