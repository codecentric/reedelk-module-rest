package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiResponse.class, scope = PROTOTYPE)
public class OpenApiResponse implements Implementor {

    @Property("Description")
    private String description;

    @Property("Summary")
    private String summary;

    @Property("Tags")
    @TabGroup("Tags, Responses and Headers")
    private List<String> tags;

    @Property("Responses")
    @TabGroup("Tags, Responses and Headers")
    @KeyName("Status Code")
    @ValueName("Edit Response")
    private Map<String, OpenApiResponseDefinition> responses = new HashMap<>();

    @Property("Headers")
    @TabGroup("Tags, Responses and Headers")
    @KeyName("Header Name")
    @ValueName("Header Value")
    private Map<String, OpenApiHeaderDefinition> headers = new HashMap<>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, OpenApiResponseDefinition> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, OpenApiResponseDefinition> responses) {
        this.responses = responses;
    }

    public Map<String, OpenApiHeaderDefinition> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, OpenApiHeaderDefinition> headers) {
        this.headers = headers;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
