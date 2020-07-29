package com.reedelk.rest.internal.openapi;

import com.reedelk.openapi.v3.ComponentsObject;
import com.reedelk.openapi.v3.OpenApiObject;
import com.reedelk.openapi.v3.PathsObject;
import com.reedelk.openapi.v3.SchemaObject;
import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.v3.OpenApiSerializableContext;
import com.reedelk.rest.component.listener.openapi.v3.OperationObject;
import com.reedelk.rest.component.listener.openapi.v3.OperationObjectUtils;
import com.reedelk.rest.internal.commons.HttpHeader;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.internal.server.HttpRequestHandler;
import com.reedelk.rest.internal.server.RouteDefinition;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.*;

public class OpenApiRequestHandler implements HttpRequestHandler {

    private static final String HTTP_PATH_SEPARATOR = "/";

    private final Formatter formatter;
    private final RESTListenerConfiguration configuration;
    private final List<RouteDefinition> routeDefinitionList = new ArrayList<>();

    protected OpenApiRequestHandler(RESTListenerConfiguration configuration, Formatter formatter) {
        this.configuration = configuration;
        this.formatter = formatter;
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        OpenApiSerializableContext context = new OpenApiSerializableContext();
        OpenApiObject openAPI = configuration.getOpenApi().map(context);

        PathsObject pathsObject = openAPI.getPaths();

        // For each route definition in the list:
        routeDefinitionList.forEach(routeDefinition -> {
            String path = routeDefinition.getPath();
            RestMethod method = routeDefinition.getMethod();
            Response successResponse = routeDefinition.getResponse();
            ErrorResponse errorResponse = routeDefinition.getErrorResponse();
            OperationObject operationObject = routeDefinition.getOpenApiObject();

            Boolean excludeApiPath = shouldExcludeApiPath(operationObject);

            OperationObject realOperationObject = operationObject == null ? new OperationObject() : operationObject;

            if (!excludeApiPath) {
                // If the 'exclude' property is true, we don't add the path.
                // Otherwise we add the path to the open API specification.
                OperationObjectUtils.addRequestParameters(realOperationObject, path);
                OperationObjectUtils.addSuccessResponse(realOperationObject, successResponse);
                OperationObjectUtils.addErrorResponse(realOperationObject, errorResponse);

                // Add Operation to path.
                Map<com.reedelk.openapi.v3.RestMethod, com.reedelk.openapi.v3.OperationObject> operationsByPath = findOperationByPath(pathsObject, path);
                operationsByPath.put(com.reedelk.openapi.v3.RestMethod.valueOf(method.name()), realOperationObject.map(context));
            }
            // ----
        });

        ComponentsObject components = openAPI.getComponents();
        context.getSchemas().forEach((schemaId, schema) -> {
            if (!components.getSchemas().containsKey(schemaId)) {
                SchemaObject schemaObject = new SchemaObject();
                schemaObject.setSchema(schema);
                components.getSchemas().put(schemaId, schemaObject);
            }
        });

        String serializedOpenAPI = formatter.format(openAPI);

        // Content Type depends on the formatter. It could be 'application/json' or 'application/x-yaml'.
        response.addHeader(HttpHeader.CONTENT_TYPE, formatter.contentType());
        response.addHeader(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(serializedOpenAPI.getBytes()));
    }

    public void add(RouteDefinition routeDefinition) {
        routeDefinitionList.add(routeDefinition);
    }

    public void remove(RouteDefinition routeDefinition) {
        routeDefinitionList.remove(routeDefinition);
    }

    private Map<com.reedelk.openapi.v3.RestMethod, com.reedelk.openapi.v3.OperationObject> findOperationByPath(PathsObject pathsObject, String path) {
        Map<String, Map<com.reedelk.openapi.v3.RestMethod, com.reedelk.openapi.v3.OperationObject>> paths = pathsObject.getPaths();
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
