package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiConfiguration.class, scope = PROTOTYPE)
public class OpenApiConfiguration implements Implementor {

    @Property("Exclude")
    private Boolean exclude;

    @Property("Summary")
    private String summary;

    @Property("Description")
    private String description;

    @Property("Operation ID")
    private String operationId;

    @Property("Request")
    private OpenApiRequest request;

    @Property("Response")
    private OpenApiResponse response;

    public Boolean getExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public OpenApiRequest getRequest() {
        return request;
    }

    public void setRequest(OpenApiRequest request) {
        this.request = request;
    }

    public OpenApiResponse getResponse() {
        return response;
    }

    public void setResponse(OpenApiResponse response) {
        this.response = response;
    }
}
