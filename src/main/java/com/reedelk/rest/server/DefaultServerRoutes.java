package com.reedelk.rest.server;

import com.reedelk.runtime.api.exception.ESBException;
import io.netty.handler.codec.http.HttpMethod;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.requireNonNull;

public class DefaultServerRoutes implements HttpServerRoutes {

    private final CopyOnWriteArrayList<HttpRouteHandler> handlers = new CopyOnWriteArrayList<>();

    @Override
    public HttpRouteHandler route(HttpPredicate condition, HttpRequestHandler handler) {
        requireNonNull(condition, "condition");
        requireNonNull(handler, "handler");

        if (isDefined(condition.method, condition.uri)) {
            throw new RouteAlreadyDefinedException(condition.method.name(), condition.uri);
        }

        HttpRouteHandler routeHandler = new HttpRouteHandler(condition, handler, condition);

        handlers.add(routeHandler);
        return routeHandler;
    }

    @Override
    public void remove(HttpMethod method, String path) {
        handlers.stream()
                .filter(handler -> handler.matchesExactly(method, path))
                .findFirst()
                .ifPresent(handlers::remove);
    }

    @Override
    public List<HttpRouteHandler> handlers() {
        return Collections.unmodifiableList(handlers);
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        final Iterator<HttpRouteHandler> iterator = handlers.iterator();
        HttpRouteHandler handler;
        try {
            while (iterator.hasNext()) {
                handler = iterator.next();
                if (handler.test(request)) {
                    return handler.apply(request, response);
                }
            }
        } catch (Throwable throwable) {
            Exceptions.throwIfFatal(throwable);
            return Mono.error(throwable); //500
        }

        return response.sendNotFound();
    }

    private boolean isDefined(HttpMethod method, String path) {
        return handlers.stream()
                .anyMatch(handler -> handler.matchesExactly(method, path));
    }


    static class RouteAlreadyDefinedException extends ESBException {
        RouteAlreadyDefinedException(String method, String path) {
            super(String.format("Route for method [%s] and path [%s] is already defined", method, path));
        }
    }
}
