package com.reedelk.rest.internal.openapi;

import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.internal.server.HttpRequestHandler;
import com.reedelk.rest.internal.server.HttpRouteHandler;
import com.reedelk.rest.internal.server.RouteDefinition;
import com.reedelk.rest.internal.server.Server;

import java.util.List;

import static com.reedelk.rest.internal.commons.RestMethod.GET;

public class OpenApiServerDecorator implements Server {

    private static final String openAPIDocumentJSON = "/openapi.json";
    private static final String openAPIDocumentYAML = "/openapi.yaml";

    private final Server delegate;
    private final RouteDefinition openAPIDocumentJSONRoute;
    private final RouteDefinition openAPIDocumentYAMLRoute;
    private final OpenApiRequestHandler openApiJsonRequestHandler;
    private final OpenApiRequestHandler openApiYamlRequestHandler;

    public OpenApiServerDecorator(RESTListenerConfiguration configuration, Server delegate) {
        this.delegate = delegate;
        this.openApiJsonRequestHandler = new OpenApiRequestHandler(configuration, Formatter.JSON);
        this.openApiYamlRequestHandler = new OpenApiRequestHandler(configuration, Formatter.YAML);

        // Add open API documents routes.
        // Response, error response and operation object are NOT used by the delegate.
        this.openAPIDocumentJSONRoute = new RouteDefinition(openAPIDocumentJSON, GET);
        this.openAPIDocumentYAMLRoute = new RouteDefinition(openAPIDocumentYAML, GET);

        delegate.addRoute(openAPIDocumentJSONRoute, openApiJsonRequestHandler);
        delegate.addRoute(openAPIDocumentYAMLRoute, openApiYamlRequestHandler);
    }

    @Override
    public void addRoute(RouteDefinition routeDefinition, HttpRequestHandler httpHandler) {
        openApiJsonRequestHandler.add(routeDefinition);
        openApiYamlRequestHandler.add(routeDefinition);
        delegate.addRoute(routeDefinition, httpHandler);
    }

    @Override
    public void removeRoute(RouteDefinition routeDefinition) {
        openApiJsonRequestHandler.remove(routeDefinition);
        openApiYamlRequestHandler.remove(routeDefinition);
        delegate.removeRoute(routeDefinition);
    }

    @Override
    public String getBasePath() {
        return delegate.getBasePath();
    }

    @Override
    public boolean hasEmptyRoutes() {
        List<HttpRouteHandler> handlers = delegate.handlers();
        // If there is only one route left, it means that there is only the open API route registered.
        // Therefore the server can be safely shutdown since it would be an empty Open API doucment.
        return handlers.size() == 1;
    }

    @Override
    public List<HttpRouteHandler> handlers() {
        return delegate.handlers();
    }

    @Override
    public void stop() {
        delegate.removeRoute(openAPIDocumentJSONRoute);
        delegate.removeRoute(openAPIDocumentYAMLRoute);
        delegate.stop();
    }
}
