package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class OperationObject implements Serializable {

    private String summary;
    private String description;
    private String operationId;
    private RequestBodyObject requestBody;
    private ResponsesObject responses;

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

    public ResponsesObject getResponses() {
        return responses;
    }

    public void setResponses(ResponsesObject responses) {
        this.responses = responses;
    }

    @Override
    public JSONObject serialize() {
        JSONObject operation = JsonObjectFactory.newJSONObject();
        operation.put("summary", summary);
        operation.put("description", description);
        operation.put("operationId", operationId);
        if (requestBody != null) {
            operation.put("requestBody", requestBody.serialize());
        }
        if (responses != null) {
            operation.put("responses", responses.serialize());
        }
        return operation;
    }
}
