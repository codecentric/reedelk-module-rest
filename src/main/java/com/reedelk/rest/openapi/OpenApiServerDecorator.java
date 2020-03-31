package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.RestListenerConfiguration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.rest.server.HttpRouteHandler;
import com.reedelk.rest.server.Server;

import java.util.List;

import static com.reedelk.rest.commons.RestMethod.GET;

public class OpenApiServerDecorator implements Server {

    private static final String openAPIDocument = "/openapi.json";

    private final Server delegate;
    private OpenApiRequestHandler openApiRequestHandler;

    public OpenApiServerDecorator(RestListenerConfiguration configuration, Server delegate) {
        this.delegate = delegate;
        this.openApiRequestHandler = new OpenApiRequestHandler(configuration);
        addOpenApiDocumentRoute();
    }

    @Override
    public void addRoute(String path, RestMethod method, Response response, ErrorResponse errorResponse, OperationObject operationObject, HttpRequestHandler httpHandler) {
        openApiRequestHandler.add(path, method, response, errorResponse, operationObject);
        delegate.addRoute(path, method, response, errorResponse, operationObject, httpHandler);
    }

    @Override
    public void removeRoute(String path, RestMethod method) {
        openApiRequestHandler.remove(path, method);
        delegate.removeRoute(path, method);
    }

    @Override
    public String getBasePath() {
        return delegate.getBasePath();
    }

    @Override
    public boolean hasEmptyRoutes() {
        // TODO: This is not correct ?!?
        List<HttpRouteHandler> handlers = delegate.handlers();
        return handlers.size() == 0 || handlers.size() == 1;
    }

    @Override
    public List<HttpRouteHandler> handlers() {
        return delegate.handlers();
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    private void addOpenApiDocumentRoute() {
        // Response, error response and operation object are not used by the delegate.
        delegate.addRoute(openAPIDocument, GET, null, null, null, openApiRequestHandler);
    }
}
