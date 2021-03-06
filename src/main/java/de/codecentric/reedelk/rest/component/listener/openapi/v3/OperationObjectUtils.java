package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.rest.internal.server.uri.UriTemplateStructure;

import java.util.List;

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
}
