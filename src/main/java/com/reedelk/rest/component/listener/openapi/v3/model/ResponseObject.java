package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Collapsible
@Component(service = ResponseObject.class, scope = ServiceScope.PROTOTYPE)
public class ResponseObject implements Implementor {

    @Property("Description")
    @Hint("Successful Response")
    @Description("A short description of the response.")
    private String description;

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

    public com.reedelk.runtime.openapi.v3.model.ResponseObject map() {
        com.reedelk.runtime.openapi.v3.model.ResponseObject target =
                new com.reedelk.runtime.openapi.v3.model.ResponseObject();
        target.setDescription(description);
        Map<String, com.reedelk.runtime.openapi.v3.model.MediaTypeObject> mappedContent = new HashMap<>();
        content.forEach(new BiConsumer<String, MediaTypeObject>() {
            @Override
            public void accept(String contentType, MediaTypeObject mediaTypeObject) {
                mappedContent.put(contentType, mediaTypeObject.map());
            }
        });
        return null;
    }
}
