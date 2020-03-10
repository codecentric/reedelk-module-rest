package com.reedelk.rest.server;

import com.reedelk.rest.commons.RestMethod;

import java.util.List;

public interface Server {

    void addRoute(RestMethod method, String path, HttpRequestHandler httpHandler);

    void removeRoute(RestMethod method, String path);

    String getBasePath();

    boolean hasEmptyRoutes();

    List<HttpRouteHandler> handlers();

    void stop();

}
