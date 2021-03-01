package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component(service = OperationObject.class, scope = ServiceScope.PROTOTYPE)
public class OperationObject implements Implementor, OpenAPIModel<de.codecentric.reedelk.openapi.v3.model.OperationObject> {

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
    @DialogTitle("Response")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private Map<String, ResponseObject> responses = new HashMap<>();

    @Property("Parameters")
    @TabGroup("Parameters Definitions and Tags")
    @ListDisplayProperty("name")
    @DialogTitle("Parameter")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private List<ParameterObject> parameters = new ArrayList<>();

    @Property("Security Requirements")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @DialogTitle("Security Requirement")
    @ListDisplayProperty("name")
    private List<SecurityRequirementObject> security = new ArrayList<>();

    @Property("Tags")
    @Hint("Tag name")
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

    public List<SecurityRequirementObject> getSecurity() {
        return security;
    }

    public void setSecurity(List<SecurityRequirementObject> security) {
        this.security = security;
    }

    @Override
    public de.codecentric.reedelk.openapi.v3.model.OperationObject map(OpenApiSerializableContext context) {
        de.codecentric.reedelk.openapi.v3.model.OperationObject target =
                new de.codecentric.reedelk.openapi.v3.model.OperationObject();

        target.setDeprecated(deprecated);
        target.setSummary(summary);
        target.setDescription(description);
        target.setOperationId(operationId);

        // Request Body
        if (requestBody != null) {
            de.codecentric.reedelk.openapi.v3.model.RequestBodyObject mappedRequestBody = requestBody.map(context);
            target.setRequestBody(mappedRequestBody);
        }

        // Responses
        Map<String, de.codecentric.reedelk.openapi.v3.model.ResponseObject> mappedResponses = new HashMap<>();
        responses.forEach((responseStatusCode, responseObject) ->
                mappedResponses.put(responseStatusCode, responseObject.map(context)));
        target.setResponses(mappedResponses);

        // Parameters
        List<de.codecentric.reedelk.openapi.v3.model.ParameterObject> mappedParameters =
                parameters.stream().map(parameterObject -> parameterObject.map(context)).collect(toList());
        target.setParameters(mappedParameters);

        // Tags
        target.setTags(tags);

        // Security
        List<Map<String, de.codecentric.reedelk.openapi.v3.model.SecurityRequirementObject>> mapped =
                security.stream().map(securityRequirementObject -> {
                    Map<String, de.codecentric.reedelk.openapi.v3.model.SecurityRequirementObject> mappedSecurity = new HashMap<>();
                    de.codecentric.reedelk.openapi.v3.model.SecurityRequirementObject mappedRequirement = securityRequirementObject.map(context);
                    mappedSecurity.put(securityRequirementObject.getName(), mappedRequirement);
                    return mappedSecurity;
                }).collect(toList());
        target.setSecurity(mapped);
        return target;
    }
}
