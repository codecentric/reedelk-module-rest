package com.reedelk.rest.internal.server;

import io.netty.handler.codec.http.HttpMethod;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.List;
import java.util.function.BiFunction;

public interface HttpServerRoutes extends BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    default HttpRouteHandler get(String path, HttpRequestHandler handler) {
        return route(HttpPredicate.get(path), handler);
    }

    default HttpRouteHandler post(String path, HttpRequestHandler handler) {
        return route(HttpPredicate.post(path), handler);
    }

    default HttpRouteHandler put(String path, HttpRequestHandler handler) {
        return route(HttpPredicate.put(path), handler);
    }

    default HttpRouteHandler delete(String path, HttpRequestHandler handler) {
        return route(HttpPredicate.delete(path), handler);
    }

    default HttpRouteHandler head(String path, HttpRequestHandler handler) {
        return route(HttpPredicate.head(path), handler);
    }

    default HttpRouteHandler options(String path, HttpRequestHandler handler) {
        return route(HttpPredicate.options(path), handler);
    }

    HttpRouteHandler route(HttpPredicate condition, HttpRequestHandler handler);

    void remove(HttpMethod method, String path);

    List<HttpRouteHandler> handlers();

}
