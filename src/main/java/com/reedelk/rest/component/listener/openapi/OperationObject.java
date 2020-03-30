package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.List;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OperationObject.class, scope = PROTOTYPE)
public class OperationObject implements Implementor, OpenApiSerializable {

    @Property("Summary")
    @Hint("Updates a pet")
    @Example("Updates a pet")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("A short summary of what the operation does.")
    private String summary;

    @Property("Description")
    @Hint("Updates a pet in the store with form data")
    @Example("Updates a pet in the store with form data")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("A verbose explanation of the operation behavior.")
    private String description;

    @Property("Operation ID")
    @Hint("updatePetWithForm")
    @Example("updatePetWithForm")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("Unique string used to identify the operation. The id MUST be unique among all operations described in the API. " +
            "Tools and libraries MAY use the operationId to uniquely identify an operation, therefore, it is RECOMMENDED to follow common programming naming conventions.")
    private String operationId;

    @Property("Exclude")
    @Description("Excludes this endpoint from being published in the OpenAPI document.")
    private Boolean exclude;

    @Property("Deprecated")
    @DefaultValue("false")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    @Description("Declares this operation to be deprecated. Consumers SHOULD refrain from usage of the declared operation.")
    private Boolean deprecated;

    @Property("Request")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private RequestBodyObject requestBody;

    @Property("Response")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private ResponseObject response;

    @Property("Parameters")
    @TabGroup("Parameters Definitions and Tags")
    @KeyName("Parameter Name")
    @ValueName("Edit Parameter")
    @DialogTitle("Parameter Definition")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private Map<String, ParameterObject> parameters;

    @Property("Tags")
    @Hint("Tag name")
    @DialogTitle("Tag")
    @TabGroup("Parameters Definitions and Tags")
    @When(propertyName = "exclude", propertyValue = "false")
    @When(propertyName = "exclude", propertyValue = When.NULL)
    private List<String> tags;

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

    public ResponseObject getResponse() {
        return response;
    }

    public void setResponse(ResponseObject response) {
        this.response = response;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Map<String, ParameterObject> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, ParameterObject> parameters) {
        this.parameters = parameters;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public JSONObject serialize() {
        JSONObject operation = JsonObjectFactory.newJSONObject();
        operation.put("summary", summary);
        operation.put("description", description);
        operation.put("operationId", operationId);
        if (tags != null) {
            JSONArray tagsArray = new JSONArray();
            tags.forEach(tagsArray::put);
            operation.put("tags", tagsArray);
        }
        if (parameters != null) {
            JSONArray paramsArray = new JSONArray();
            //parameters.forEach(parameterObject -> {
              //  JSONObject item = parameterObject.serialize();
//                paramsArray.put(item);
  //          });
            operation.put("parameters", parameters);
        }
        if (requestBody != null) {
            operation.put("requestBody", requestBody.serialize());
        }
       // if (responses != null) {
         //   operation.put("responses", responses.serialize());
        //}
        return operation;
    }
}
