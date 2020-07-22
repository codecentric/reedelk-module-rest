package com.reedelk.rest.internal.server;

import java.util.List;

public interface Server {

    String getBasePath();

    boolean hasEmptyRoutes();

    List<HttpRouteHandler> handlers();

    void stop();

    void addRoute(RouteDefinition routeDefinition, HttpRequestHandler httpHandler);

    void removeRoute(RouteDefinition routeDefinition);
}
