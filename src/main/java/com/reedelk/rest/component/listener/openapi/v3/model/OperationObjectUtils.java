package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.internal.commons.Messages;
import com.reedelk.rest.internal.server.uri.UriTemplateStructure;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicInteger;

import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class OperationObjectUtils {

    /**
     * Adds auto generated parameters from request path.
     */
    public static void addRequestParameters(OperationObject operationObject, String path) {
        UriTemplateStructure templateStructure = UriTemplateStructure.from(path);
        List<String> requestPathParams = templateStructure.getVariableNames();
        List<ParameterObject> parameters = operationObject.getParameters();

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

    /**
     * Adds success response.
     */
    public static void addSuccessResponse(OperationObject givenOperation, Response response) {
        if (response == null) return;

        DynamicInteger responseStatus = response.getStatus();
        if (responseStatus == null) return;

        String description = Messages.RestListener.OPEN_API_SUCCESS_RESPONSE.format();
        DynamicStringMap responseHeaders = response.getHeaders();
        Map<String, ResponseObject> responses = givenOperation.getResponses();

        addResponse(responseStatus, responses, responseHeaders, description);
    }

    /**
     * Adds default error response.
     */
    public static void addErrorResponse(OperationObject givenOperation, ErrorResponse errorResponse) {
        if (errorResponse == null) return;

        DynamicInteger errorResponseStatus = errorResponse.getStatus();
        if (errorResponseStatus == null) return;

        String description = Messages.RestListener.OPEN_API_ERROR_RESPONSE.format();
        DynamicStringMap errorResponseHeaders = errorResponse.getHeaders();
        Map<String, ResponseObject> responses = givenOperation.getResponses();

        addResponse(errorResponseStatus, responses, errorResponseHeaders, description);
    }

    private static void addResponse(DynamicInteger status, Map<String, ResponseObject> responses, DynamicStringMap headers, String description) {
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

    private static void applyDefaultHeaders(ResponseObject responseObject, DynamicStringMap headers) {
        headers.keySet().forEach(headerName -> {
            Map<String, HeaderObject> headersMap = responseObject.getHeaders();
            if (!headersMap.containsKey(headerName)) {
                HeaderObject headerObject = new HeaderObject();
                headersMap.put(headerName, headerObject);
            }
        });
    }
}
