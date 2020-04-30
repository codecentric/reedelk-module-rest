package com.reedelk.rest.internal.server;

import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.internal.commons.RestMethod;

import java.util.List;

public interface Server {

    String getBasePath();

    boolean hasEmptyRoutes();

    List<HttpRouteHandler> handlers();

    void stop();

    void addRoute(String path, RestMethod method, Response response, ErrorResponse errorResponse, OperationObject operationObject, HttpRequestHandler httpHandler);

    void removeRoute(String path, RestMethod method);

}
