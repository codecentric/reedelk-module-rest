package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.rest.server.HttpRouteHandler;
import com.reedelk.rest.server.Server;

import java.util.List;

public class OpenAPIServerDecorator implements Server {

    private static final String openAPIDocument = "openapi.json";

    private final Server delegate;
    private OpenAPIRequestHandler handler;

    public OpenAPIServerDecorator(Server delegate) {
        this.delegate = delegate;
        handler = new OpenAPIRequestHandler();

        OpenApiConfiguration configuration = new OpenApiConfiguration();
        configuration.setExclude(true);
        delegate.addRoute("/" + openAPIDocument, RestMethod.GET, configuration, handler);
    }

    @Override
    public void addRoute(String path, RestMethod method, OpenApiConfiguration openApiConfiguration, HttpRequestHandler httpHandler) {
        handler.add(path, method, openApiConfiguration);
        delegate.addRoute(path, method, openApiConfiguration, httpHandler);
    }

    @Override
    public void removeRoute(String path, RestMethod method) {
        handler.remove(path, method);
        delegate.removeRoute(path, method);
    }

    @Override
    public String getBasePath() {
        return delegate.getBasePath();
    }

    @Override
    public boolean hasEmptyRoutes() {
        // TODO: Hmmmm revise this....
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
