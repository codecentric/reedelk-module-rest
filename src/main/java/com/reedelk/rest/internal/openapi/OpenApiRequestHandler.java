package com.reedelk.rest.internal.openapi;

import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.v3.model.OperationObject;
import com.reedelk.rest.component.listener.openapi.v3.model.OperationObjectUtils;
import com.reedelk.rest.internal.commons.HttpHeader;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.internal.server.HttpRequestHandler;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OpenApiRequestHandler implements HttpRequestHandler {

    private static final String HTTP_PATH_SEPARATOR = "/";
    private final Formatter formatter;

    protected com.reedelk.runtime.openapi.v3.model.OpenApiObject openAPI;
    protected OpenApiSerializableContext context;

    protected OpenApiRequestHandler(RESTListenerConfiguration configuration, Formatter formatter) {
        this.context = new OpenApiSerializableContext();
        this.openAPI = configuration.getOpenApi().map(context);
        this.openAPI.setBasePath(configuration.getBasePath());
        this.formatter = formatter;
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        String openApiAsJson = serializeOpenAPI();
        response.addHeader(HttpHeader.CONTENT_TYPE, MimeType.APPLICATION_JSON.toString());
        response.addHeader(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(openApiAsJson.getBytes()));
    }

    public void add(String path, RestMethod httpMethod, Response response, ErrorResponse errorResponse, OperationObject operationObject) {
        Boolean excludeApiPath = shouldExcludeApiPath(operationObject);

        OperationObject realOperationObject = operationObject == null ? new OperationObject() : operationObject;

        if (!excludeApiPath) {
            // If the 'exclude' property is true, we don't add the path.
            // Otherwise we add the path to the open API specification.
            OperationObjectUtils.addRequestParameters(realOperationObject, path);
            OperationObjectUtils.addSuccessResponse(realOperationObject, response);
            OperationObjectUtils.addErrorResponse(realOperationObject, errorResponse);

            // Add Operation to path.
            Map<com.reedelk.runtime.openapi.v3.model.RestMethod, com.reedelk.runtime.openapi.v3.model.OperationObject> operationsByPath = findOperationByPath(path);
            operationsByPath.put(com.reedelk.runtime.openapi.v3.model.RestMethod.valueOf(httpMethod.name()), realOperationObject.map(context));
        }
    }

    public void remove(String path, RestMethod restMethod) {
        // Remove Operation from path.
        Map<com.reedelk.runtime.openapi.v3.model.RestMethod, com.reedelk.runtime.openapi.v3.model.OperationObject> operationsByPath = findOperationByPath(path);
        operationsByPath.remove(com.reedelk.runtime.openapi.v3.model.RestMethod.valueOf(restMethod.name()));
        if (operationsByPath.isEmpty()) {
            String pathToRemove = realPathOf(path);
            com.reedelk.runtime.openapi.v3.model.PathsObject pathsObject = openAPI.getPaths();
            pathsObject.getPaths().remove(pathToRemove);
        }
    }

    String serializeOpenAPI() {
        return formatter.format(openAPI, context);
    }

    private Map<com.reedelk.runtime.openapi.v3.model.RestMethod, com.reedelk.runtime.openapi.v3.model.OperationObject> findOperationByPath(String path) {
        com.reedelk.runtime.openapi.v3.model.PathsObject pathsObject = openAPI.getPaths();
        Map<String, Map<com.reedelk.runtime.openapi.v3.model.RestMethod, com.reedelk.runtime.openapi.v3.model.OperationObject>> paths = pathsObject.getPaths();
        String fixedPath = realPathOf(path);
        if (!paths.containsKey(fixedPath)) {
            paths.put(fixedPath, new HashMap<>());
        }
        return paths.get(fixedPath);
    }

    private String realPathOf(String path) {
        return path == null ? HTTP_PATH_SEPARATOR : path;
    }

    private Boolean shouldExcludeApiPath(OperationObject operationObject) {
        return Optional.ofNullable(operationObject)
                .flatMap(config -> Optional.ofNullable(config.getExclude()))
                .orElse(false);
    }
}
