package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

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

    public de.codecentric.reedelk.openapi.v3.model.ResponseObject map(OpenApiSerializableContext context) {
        de.codecentric.reedelk.openapi.v3.model.ResponseObject target =
                new de.codecentric.reedelk.openapi.v3.model.ResponseObject();
        target.setDescription(description);

        // Content
        Map<String, de.codecentric.reedelk.openapi.v3.model.MediaTypeObject> mappedContent = new HashMap<>();
        content.forEach((contentType, mediaTypeObject) -> mappedContent.put(contentType, mediaTypeObject.map(context)));
        target.setContent(mappedContent);

        // Headers
        Map<String, de.codecentric.reedelk.openapi.v3.model.HeaderObject> mappedHeaders = new HashMap<>();
        headers.forEach((headerName, headerObject) -> mappedHeaders.put(headerName, headerObject.map(context)));
        target.setHeaders(mappedHeaders);

        return target;
    }
}
