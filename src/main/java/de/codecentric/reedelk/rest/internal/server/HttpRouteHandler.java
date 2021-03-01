package de.codecentric.reedelk.rest.internal.server;

import io.netty.handler.codec.http.HttpMethod;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class HttpRouteHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    private final HttpPredicate predicateMatcher;
    private final HttpRequestHandler handler;

    HttpRouteHandler(HttpPredicate predicateMatcher, HttpRequestHandler handler) {
        this.predicateMatcher = requireNonNull(predicateMatcher, "predicateMatcher");
        this.handler = requireNonNull(handler, "handler");
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        return handler.apply(request.paramsResolver(predicateMatcher), response);
    }

    public HttpPredicate.MatcherResult matches(HttpServerRequest request) {
        return predicateMatcher.matches(request.method(), request.uri());
    }

    public HttpPredicate.MatcherResult matches(HttpMethod method, String uri) {
        return predicateMatcher.matches(method, uri);
    }
}
