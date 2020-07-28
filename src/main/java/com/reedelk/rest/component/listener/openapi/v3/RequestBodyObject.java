package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Collapsible
@Component(service = RequestBodyObject.class, scope = ServiceScope.PROTOTYPE)
public class RequestBodyObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.RequestBodyObject> {

    @Property("Required")
    @DefaultValue("false")
    @Description("Determines if the request body is required in the request.")
    private Boolean required;

    @Property("Description")
    @Hint("My request body")
    @Description("A brief description of the request body. This could contain examples of use.")
    private String description;

    @Property("Request")
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
    public com.reedelk.openapi.v3.RequestBodyObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.RequestBodyObject mappedRequestBody =
                new com.reedelk.openapi.v3.RequestBodyObject();
        mappedRequestBody.setRequired(required);
        mappedRequestBody.setDescription(description);

        // Content
        Map<String, com.reedelk.openapi.v3.MediaTypeObject> mappedContent = new HashMap<>();
        content.forEach((contentType, mediaTypeObject) -> mappedContent.put(contentType, mediaTypeObject.map(context)));
        mappedRequestBody.setContent(mappedContent);

        return mappedRequestBody;
    }
}
