package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.When;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OpenApiConfiguration.class, scope = PROTOTYPE)
public class OpenApiConfiguration implements Implementor {

    @Property("Exclude")
    private Boolean exclude;

    @Property("Summary")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private String summary;

    @Property("Description")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private String description;

    @Property("Operation ID")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private String operationId;

    @Property("Request")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private OpenApiRequest request;

    @Property("Response")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private OpenApiResponse response;

    @Property("Parameters")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private OpenApiParameters parameters;

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

    public OpenApiParameters getParameters() {
        return parameters;
    }

    public void setParameters(OpenApiParameters parameters) {
        this.parameters = parameters;
    }
}
