package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.RestListenerConfiguration;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.rest.server.HttpRouteHandler;
import com.reedelk.rest.server.Server;

import java.util.List;

import static com.reedelk.rest.commons.RestMethod.GET;

public class OpenAPIServerDecorator1 implements Server {

    private static final String openAPIDocument = "/openapi.json";

    private final Server delegate;
    private OpenAPIRequestHandler1 openAPIRequestHandler1;

    public OpenAPIServerDecorator1(RestListenerConfiguration configuration, Server delegate) {
        this.delegate = delegate;
        this.openAPIRequestHandler1 = new OpenAPIRequestHandler1(configuration);
        addOpenApiDocumentRoute();
    }

    @Override
    public void addRoute(String path, RestMethod method, OperationObject operationObject, HttpRequestHandler httpHandler) {
        openAPIRequestHandler1.add(path, method, operationObject);
        delegate.addRoute(path, method, operationObject, httpHandler);
    }

    @Override
    public void removeRoute(String path, RestMethod method) {
        openAPIRequestHandler1.remove(path, method);
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

    private void addOpenApiDocumentRoute() {
        OperationObject operationObject = new OperationObject();
        delegate.addRoute(openAPIDocument, GET, operationObject, openAPIRequestHandler1);
    }
}
