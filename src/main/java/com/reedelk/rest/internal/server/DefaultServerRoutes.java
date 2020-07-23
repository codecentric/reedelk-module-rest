package com.reedelk.rest.internal.server;

import com.reedelk.rest.internal.commons.Messages.RestListener;
import com.reedelk.runtime.api.exception.PlatformException;
import io.netty.handler.codec.http.HttpMethod;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.reedelk.rest.internal.server.HttpPredicate.MatcherResult;
import static java.util.Objects.requireNonNull;

public class DefaultServerRoutes implements HttpServerRoutes {

    private final CopyOnWriteArrayList<HttpRouteHandler> handlers = new CopyOnWriteArrayList<>();

    @Override
    public HttpRouteHandler route(HttpPredicate predicateMatcher, HttpRequestHandler handler) {
        requireNonNull(predicateMatcher, "predicateMatcher");
        requireNonNull(handler, "handler");

        if (isAlreadyRegistered(predicateMatcher.getMethod(), predicateMatcher.getUri())) {
            throw new RouteAlreadyDefinedException(predicateMatcher.getMethod().name(), predicateMatcher.getUri());
        }

        HttpRouteHandler routeHandler = new HttpRouteHandler(predicateMatcher, handler);

        handlers.add(routeHandler);
        return routeHandler;
    }

    @Override
    public void remove(HttpMethod method, String path) {
        handlers.stream()
                .filter(handler -> MatcherResult.EXACT_MATCH.equals(handler.matches(method, path)))
                .findFirst()
                .ifPresent(handlers::remove);
    }

    @Override
    public List<HttpRouteHandler> handlers() {
        return Collections.unmodifiableList(handlers);
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        try {
            Optional<HttpRouteHandler> matchingHandler = findMatchingHttpRouteHandler(request);
            if (matchingHandler.isPresent()) {
                return matchingHandler.get().apply(request, response);
            }
            return response.sendNotFound();
        } catch (Throwable throwable) {
            Exceptions.throwIfFatal(throwable);
            return Mono.error(throwable); //500
        }
    }

    private boolean isAlreadyRegistered(HttpMethod method, String path) {
        // We are looking for exact matches, e.g /api/{id} -> /api/{id}
        return handlers
                .stream()
                .anyMatch(handler -> MatcherResult.EXACT_MATCH.equals(handler.matches(method, path)));
    }

    /**
     * A route http handler is a match if there is first an exact match, matching the request uri.
     * If there is no an exact match we look for an uri template e.g /api/{ID}.
     */
    private Optional<HttpRouteHandler> findMatchingHttpRouteHandler(HttpServerRequest request) {
        final Iterator<HttpRouteHandler> iterator = handlers.iterator();
        HttpRouteHandler matchingHandler = null;
        while (iterator.hasNext()) {
            HttpRouteHandler handler = iterator.next();
            MatcherResult templateMatch = handler.matches(request);
            if (MatcherResult.EXACT_MATCH.equals(templateMatch)) {
                // Priority goes to exact matches.
                return Optional.of(handler);
            } else if (MatcherResult.TEMPLATE_MATCH.equals(templateMatch)) {
                // Template match only if an exact match was not found.
                matchingHandler = handler;
            }
        }
        return matchingHandler != null ? Optional.of(matchingHandler) : Optional.empty();
    }

    static class RouteAlreadyDefinedException extends PlatformException {

        RouteAlreadyDefinedException(String method, String path) {
            super(RestListener.ERROR_ROUTE_ALREADY_DEFINED.format(method, path));
        }
    }
}
