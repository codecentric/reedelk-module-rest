package com.reedelk.rest.internal.server;

import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.function.BiFunction;

public interface HttpRequestHandler extends BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {
}
