package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.rest.server.HttpRouteHandler;
import com.reedelk.rest.server.Server;

import java.util.List;

public class OpenAPIServerDecorator implements Server {

    private static final String openAPIDocument = "openapi.json";

    private final Server delegate;
    private OpenAPIHttpRequestHandler handler;

    public OpenAPIServerDecorator(Server delegate) {
        this.delegate = delegate;
        handler = new OpenAPIHttpRequestHandler();
        // TODO: Create default route for openapi.json.
        delegate.addRoute(RestMethod.GET, "/" + openAPIDocument, handler);
    }

    @Override
    public void addRoute(RestMethod method, String path, HttpRequestHandler httpHandler) {
        // TODO: When we add route we need to
        handler.add(path, method);
        delegate.addRoute(method, path, httpHandler);
    }

    @Override
    public void removeRoute(RestMethod method, String path) {
        // TODO: Update openapi.json
        delegate.removeRoute(method, path);
    }

    @Override
    public String getBasePath() {
        // TODO: Openapi.json should be on this base path.
        return delegate.getBasePath();
    }

    @Override
    public boolean hasEmptyRoutes() {
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
}
