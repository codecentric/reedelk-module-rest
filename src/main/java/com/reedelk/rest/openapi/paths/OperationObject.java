package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.function.Consumer;

public class OperationObject implements Serializable {

    private String summary;
    private String description;
    private String operationId;
    private ResponsesObject responses;
    private RequestBodyObject requestBody;
    private List<String> tags;
    private List<ParameterObject> parameters;

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

    public ResponsesObject getResponses() {
        return responses;
    }

    public void setResponses(ResponsesObject responses) {
        this.responses = responses;
    }

    public RequestBodyObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBodyObject requestBody) {
        this.requestBody = requestBody;
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
            parameters.forEach(parameterObject -> {
                JSONObject item = parameterObject.serialize();
                paramsArray.put(item);
            });
            operation.put("parameters", parameters);
        }
        if (requestBody != null) {
            operation.put("requestBody", requestBody.serialize());
        }
        if (responses != null) {
            operation.put("responses", responses.serialize());
        }
        return operation;
    }
}
