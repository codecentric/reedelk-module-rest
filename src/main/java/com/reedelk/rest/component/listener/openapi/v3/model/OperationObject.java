package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component(service = OperationObject.class, scope = ServiceScope.PROTOTYPE)
public class OperationObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.OperationObject> {

    @Property("Exclude this resource from the OpenAPI document")
    @Description("Excludes this endpoint from being published in the OpenAPI document.")
    private Boolean exclude;

    @Property("Deprecated operation")
    @DefaultValue("false")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("Declares this operation to be deprecated. Consumers SHOULD refrain from usage of the declared operation.")
    private Boolean deprecated;
    
    @Property("Summary")
    @Hint("Updates orders")
    @Example("Updates an order")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("A short summary of what the operation does.")
    private String summary;

    @Property("Description")
    @Hint("Updates an order in the store with JSON data")
    @Example("Updates an order in the store with JSON data")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("A verbose explanation of the operation behavior.")
    private String description;

    @Property("Operation ID")
    @Hint("updateOrder")
    @Example("updateOrder")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("Unique string used to identify the operation. The id MUST be unique among all operations described in the API. " +
            "Tools and libraries MAY use the operationId to uniquely identify an operation, therefore, " +
            "it is RECOMMENDED to follow common programming naming conventions.")
    private String operationId;

    @Property("Request")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private RequestBodyObject requestBody;

    @Property("Responses")
    @KeyName("Status Code")
    @ValueName("Response")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private Map<String, ResponseObject> responses = new HashMap<>();

    @Property("Parameters")
    @TabGroup("Parameters Definitions and Tags")
    @ListDisplayProperty("name")
    @DialogTitle("Parameter Configuration")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private List<ParameterObject> parameters = new ArrayList<>();

    @Property("Tags")
    @Hint("Tag name")
    @DialogTitle("Tag")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private List<String> tags = new ArrayList<>();

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

    public RequestBodyObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBodyObject requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, ResponseObject> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, ResponseObject> responses) {
        this.responses = responses;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public List<ParameterObject> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterObject> parameters) {
        this.parameters = parameters;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public com.reedelk.openapi.v3.OperationObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.OperationObject target =
                new com.reedelk.openapi.v3.OperationObject();

        target.setExclude(exclude);
        target.setDeprecated(deprecated);
        target.setSummary(summary);
        target.setDescription(description);
        target.setOperationId(operationId);

        // Request Body
        if (requestBody != null) {
            com.reedelk.openapi.v3.RequestBodyObject mappedRequestBody = requestBody.map(context);
            target.setRequestBody(mappedRequestBody);
        }

        // Responses
        Map<String, com.reedelk.openapi.v3.ResponseObject> mappedResponses = new HashMap<>();
        responses.forEach((responseStatusCode, responseObject) ->
                mappedResponses.put(responseStatusCode, responseObject.map(context)));
        target.setResponses(mappedResponses);

        // Parameters
        List<com.reedelk.openapi.v3.ParameterObject> mappedParameters =
                parameters.stream().map(parameterObject -> parameterObject.map(context)).collect(toList());
        target.setParameters(mappedParameters);

        // Tags
        target.setTags(tags);
        return target;
    }
}
