package com.reedelk.rest.server;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.openapi.OperationObject;

import java.util.List;

public interface Server {

    String getBasePath();

    boolean hasEmptyRoutes();

    List<HttpRouteHandler> handlers();

    void stop();

    void addRoute(String path, RestMethod method, OperationObject operationObject, HttpRequestHandler httpHandler);

    void removeRoute(String path, RestMethod method);

}
