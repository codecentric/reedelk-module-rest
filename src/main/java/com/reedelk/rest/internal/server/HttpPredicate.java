package com.reedelk.rest.internal.server;

import com.reedelk.rest.internal.server.uri.UriTemplate;
import com.reedelk.runtime.api.commons.Preconditions;
import io.netty.handler.codec.http.HttpMethod;

import java.util.Map;
import java.util.function.Function;

public class HttpPredicate implements Function<Object, Map<String, String>> {

    private final String uri;
    private final HttpMethod method;
    private final UriTemplate template;

    HttpPredicate(String uri, HttpMethod method) {
        Preconditions.checkNotNull(uri, "uri must not be null");
        Preconditions.checkNotNull(method, "http method must not be null");
        this.uri = uri;
        this.method = method;
        this.template = new UriTemplate(uri);
    }

    public static HttpPredicate delete(String uri) {
        return http(uri, HttpMethod.DELETE);
    }

    public static HttpPredicate get(String uri) {
        return http(uri, HttpMethod.GET);
    }

    public static HttpPredicate head(String uri) {
        return http(uri, HttpMethod.HEAD);
    }

    private static HttpPredicate http(String uri, HttpMethod method) {
        return new HttpPredicate(uri, method);
    }

    public static HttpPredicate options(String uri) {
        return http(uri, HttpMethod.OPTIONS);
    }

    public static HttpPredicate post(String uri) {
        return http(uri, HttpMethod.POST);
    }

    public static HttpPredicate put(String uri) {
        return http(uri, HttpMethod.PUT);
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public Map<String, String> apply(Object key) {
        if (template == null) return null;
        Map<String, String> headers = template.bind(key.toString());
        if (headers != null  && !headers.isEmpty()) {
            return headers;
        }
        return null;
    }

    public final MatcherResult matches(HttpMethod method, String uri) {
        if (this.method.equals(method)) {
            if (this.uri.equals(uri)) {
                // The match is exact, e.g /api/myOperation, we did not need to match against the template.
                return MatcherResult.EXACT_MATCH;
            } else if ((template == null || template.matches(uri))){
                // The match is not exact, there was a match against a template, e.g /api/{ID}
                return MatcherResult.TEMPLATE_MATCH;
            }
        }
        return MatcherResult.NO_MATCH;
    }

    enum MatcherResult {
        NO_MATCH,
        TEMPLATE_MATCH,
        EXACT_MATCH
    }
}

