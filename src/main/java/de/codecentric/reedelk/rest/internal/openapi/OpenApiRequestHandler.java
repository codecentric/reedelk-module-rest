package de.codecentric.reedelk.rest.internal.openapi;

import de.codecentric.reedelk.openapi.v3.model.*;
import de.codecentric.reedelk.rest.component.RESTListenerConfiguration;
import de.codecentric.reedelk.rest.component.listener.openapi.v3.OpenApiSerializableContext;
import de.codecentric.reedelk.rest.component.listener.openapi.v3.OperationObject;
import de.codecentric.reedelk.rest.component.listener.openapi.v3.OperationObjectUtils;
import de.codecentric.reedelk.rest.internal.commons.Defaults;
import de.codecentric.reedelk.rest.internal.commons.HttpHeader;
import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import de.codecentric.reedelk.rest.internal.server.HttpRequestHandler;
import de.codecentric.reedelk.rest.internal.server.RouteDefinition;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.*;

public class OpenApiRequestHandler implements HttpRequestHandler {

    private final Serializer serializer;
    private final RESTListenerConfiguration configuration;
    private final List<RouteDefinition> routeDefinitionList = new ArrayList<>();

    protected OpenApiRequestHandler(RESTListenerConfiguration configuration, Serializer serializer) {
        this.configuration = configuration;
        this.serializer = serializer;
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        // Prepare the context
        OpenApiSerializableContext context = new OpenApiSerializableContext();

        // OpenApi
        OpenApiObject openAPI = configuration.getOpenApi().map(context);

        // Server Object
        ServerObject defaultServerObject = DefaultServerObjectBuilder.from(configuration);
        openAPI.getServers().add(defaultServerObject);

        // Paths Object
        PathsObject pathsObject = openAPI.getPaths();
        routeDefinitionList.forEach(routeDefinition ->
                buildOperationObjectFromRoute(context, pathsObject, routeDefinition));

        // Components Object
        ComponentsObject components = openAPI.getComponents();

        // We must add all the schemas which where defined on the fly and not user defined.
        context.getSchemas().forEach((schemaId, schema) -> {
            if (!components.getSchemas().containsKey(schemaId)) {
                SchemaObject schemaObject = new SchemaObject();
                schemaObject.setSchema(schema);
                components.getSchemas().put(schemaId, schemaObject);
            }
        });

        // We must add all the Examples which were added on the fly and not user defined.
        context.getExamples().forEach((exampleId, exampleObject) ->
                components.getExamples().put(exampleId, exampleObject));

        String serializedOpenAPI = serializer.serialize(openAPI);

        // Content Type depends on the formatter. It could be 'application/json' or 'application/x-yaml'.
        response.addHeader(HttpHeader.CONTENT_TYPE, serializer.contentType());
        response.addHeader(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(serializedOpenAPI.getBytes()));
    }

    private void buildOperationObjectFromRoute(OpenApiSerializableContext context, PathsObject pathsObject, RouteDefinition routeDefinition) {
        String path = routeDefinition.getPath();
        RestMethod method = routeDefinition.getMethod();
        OperationObject operationObject = routeDefinition.getOpenApiObject();

        Boolean excludeApiPath = shouldExcludeApiPath(operationObject);
        if (!excludeApiPath) {

            OperationObject realOperationObject = operationObject == null ? new OperationObject() : operationObject;
            // If the 'exclude' property is true, we don't add the path.
            // Otherwise we add the path to the open API specification.
            OperationObjectUtils.addRequestParameters(realOperationObject, path);

            // Add Operation to path.
            Map<de.codecentric.reedelk.openapi.v3.model.RestMethod, de.codecentric.reedelk.openapi.v3.model.OperationObject> operationsByPath = findOperationByPath(pathsObject, path);
            operationsByPath.put(de.codecentric.reedelk.openapi.v3.model.RestMethod.valueOf(method.name()), realOperationObject.map(context));
        }
    }

    public void add(RouteDefinition routeDefinition) {
        routeDefinitionList.add(routeDefinition);
    }

    public void remove(RouteDefinition routeDefinition) {
        routeDefinitionList.remove(routeDefinition);
    }

    private Map<de.codecentric.reedelk.openapi.v3.model.RestMethod, de.codecentric.reedelk.openapi.v3.model.OperationObject> findOperationByPath(PathsObject pathsObject, String path) {
        Map<String, Map<de.codecentric.reedelk.openapi.v3.model.RestMethod, de.codecentric.reedelk.openapi.v3.model.OperationObject>> paths = pathsObject.getPaths();
        String fixedPath = realPathOf(path);
        if (!paths.containsKey(fixedPath)) {
            paths.put(fixedPath, new HashMap<>());
        }
        return paths.get(fixedPath);
    }

    private String realPathOf(String path) {
        return path == null ? Defaults.RestListener.pathSeparator() : path;
    }

    private Boolean shouldExcludeApiPath(OperationObject operationObject) {
        return Optional.ofNullable(operationObject)
                .flatMap(config -> Optional.ofNullable(config.getExclude()))
                .orElse(false);
    }
}
