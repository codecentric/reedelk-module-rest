package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.internal.commons.JsonObjectFactory;
import com.reedelk.rest.internal.commons.Messages;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.internal.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.internal.openapi.OpenApiSerializableContext;
import com.reedelk.rest.internal.server.uri.UriTemplateStructure;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicInteger;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Optional.ofNullable;

public class PathsObject extends AbstractOpenApiSerializable {

    private Map<String, Map<RestMethod, OperationObject>> paths = new TreeMap<>();

    @Override
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject pathsObject = JsonObjectFactory.newJSONObject();
        paths.forEach((path, pathItemObject) -> {
            JSONObject operationsByPathJsonObject = JsonObjectFactory.newJSONObject();
            pathItemObject.forEach((restMethod, operationObject) ->
                    operationsByPathJsonObject.put(restMethod.name().toLowerCase(),
                            operationObject.serialize(context)));
            pathsObject.put(path, operationsByPathJsonObject);
        });
        return pathsObject;
    }

    public void add(String path, RestMethod restMethod, Response response, ErrorResponse errorResponse, OperationObject pathItemObject) {
        Map<RestMethod, OperationObject> operationsByPath = operationsByPathOf(path);

        addDefaultParameters(pathItemObject, path);
        addDefaultResponse(pathItemObject, response); // Adding auto generated responses from request
        addDefaultResponse(pathItemObject, errorResponse); // Adding auto generated error responses from request

        operationsByPath.put(restMethod, pathItemObject);
    }

    public void add(String path, RestMethod restMethod, Response response, ErrorResponse errorResponse) {
        Map<RestMethod, OperationObject> operationsByPath = operationsByPathOf(path);
        // Create a default operation object
        OperationObject defaultOperation = new OperationObject();

        addDefaultParameters(defaultOperation, path);   // Adding auto generated parameters from request path
        addDefaultResponse(defaultOperation, response); // Adding auto generated responses from request
        addDefaultResponse(defaultOperation, errorResponse); // Adding auto generated error responses from request

        operationsByPath.put(restMethod, defaultOperation);
    }

    private void addDefaultParameters(OperationObject givenOperation, String path) {
        if (StringUtils.isBlank(path)) return;

        // Add default parameters
        UriTemplateStructure templateStructure = UriTemplateStructure.from(path);
        List<String> requestPathParams = templateStructure.getVariableNames();
        List<ParameterObject> parameters = givenOperation.getParameters();

        // We only set default parameters for param names not overridden by the user.
        requestPathParams.forEach(requestParam -> {
            boolean hasBeenUserDefined = parameters.stream().anyMatch(parameterObject ->
                    requestParam.equals(parameterObject.getName()));
            if (!hasBeenUserDefined) {
                ParameterObject parameterObject = new ParameterObject();
                parameterObject.setName(requestParam);
                parameterObject.setRequired(true);
                parameterObject.setIn(ParameterLocation.path);
                parameterObject.setStyle(ParameterStyle.simple);
                parameters.add(parameterObject);
            }
        });
    }

    private void addDefaultResponse(OperationObject givenOperation, ErrorResponse errorResponse) {
        if (errorResponse == null) return;

        DynamicInteger errorResponseStatus = errorResponse.getStatus();
        if (errorResponseStatus == null) return;

        String description = Messages.RestListener.OPEN_API_ERROR_RESPONSE.format();
        DynamicStringMap errorResponseHeaders = errorResponse.getHeaders();
        Map<String, ResponseObject> responses = givenOperation.getResponses();

        addDefaultResponse(errorResponseStatus, responses, errorResponseHeaders, description);
    }

    private void addDefaultResponse(OperationObject givenOperation, Response response) {
        if (response == null) return;

        DynamicInteger responseStatus = response.getStatus();
        if (responseStatus == null) return;

        String description = Messages.RestListener.OPEN_API_SUCCESS_RESPONSE.format();
        DynamicStringMap responseHeaders = response.getHeaders();
        Map<String, ResponseObject> responses = givenOperation.getResponses();

        addDefaultResponse(responseStatus, responses, responseHeaders, description);
    }

    private void addDefaultResponse(DynamicInteger status,
                                    Map<String, ResponseObject> responses,
                                    DynamicStringMap headers,
                                    String description) {
        // If the return status is a script, we cannot infer so we won't add the entry.
        if (status.isScript()) return;

        ofNullable(status.value()).ifPresent(errorResponseStatusValue -> {
            // if response contains Content-Type header we use that one
            String errorResponseStatusAsString = String.valueOf(errorResponseStatusValue);

            if (!responses.containsKey(errorResponseStatusAsString)) {
                // We only put it if the user has not defined his/her
                // own response for the current status code.
                ResponseObject responseObject = new ResponseObject();
                responseObject.setDescription(description);
                responses.put(errorResponseStatusAsString, responseObject);
            }

            ResponseObject responseObject = responses.get(errorResponseStatusAsString);
            applyDefaultHeaders(responseObject, headers);
        });
    }

    private void applyDefaultHeaders(ResponseObject responseObject, DynamicStringMap headers) {
        headers.keySet().forEach(headerName -> {
            Map<String, HeaderObject> headersMap = responseObject.getHeaders();
            if (!headersMap.containsKey(headerName)) {
                HeaderObject headerObject = new HeaderObject();
                headersMap.put(headerName, headerObject);
            }
        });
    }

    public void remove(String path, RestMethod restMethod) {
        Map<RestMethod, OperationObject> operationsByPath = operationsByPathOf(path);
        operationsByPath.remove(restMethod);
        if (operationsByPath.isEmpty()) {
            String pathToRemove = realPathOf(path);
            paths.remove(pathToRemove);
        }
    }

    private Map<RestMethod, OperationObject> operationsByPathOf(String path) {
        String fixedPath = realPathOf(path);
        if (!paths.containsKey(fixedPath)) {
            paths.put(fixedPath, new HashMap<>());
        }
        return paths.get(fixedPath);
    }

    private String realPathOf(String path) {
        return path == null ? "/" : path;
    }
}
