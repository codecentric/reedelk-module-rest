package de.codecentric.reedelk.rest.internal.attribute;

import de.codecentric.reedelk.rest.internal.commons.HttpHeader;
import de.codecentric.reedelk.rest.internal.server.mapper.HttpRequestWrapper;
import de.codecentric.reedelk.runtime.api.annotation.Type;
import de.codecentric.reedelk.runtime.api.annotation.TypeProperty;
import de.codecentric.reedelk.runtime.api.message.MessageAttributes;
import de.codecentric.reedelk.runtime.api.type.MapOfStringListOfString;
import de.codecentric.reedelk.runtime.api.type.MapOfStringString;

import static de.codecentric.reedelk.rest.internal.attribute.RESTListenerAttributes.*;
import static de.codecentric.reedelk.runtime.api.message.MessageAttributeKey.CORRELATION_ID;

@Type
@TypeProperty(name = REMOTE_ADDRESS, type = String.class)
@TypeProperty(name = MATCHING_PATH, type = String.class)
@TypeProperty(name = QUERY_PARAMS, type = MapOfStringListOfString.class)
@TypeProperty(name = REQUEST_PATH, type = String.class)
@TypeProperty(name = REQUEST_URI, type = String.class)
@TypeProperty(name = QUERY_STRING, type = String.class)
@TypeProperty(name = PATH_PARAMS, type = MapOfStringString.class)
@TypeProperty(name = VERSION, type = String.class)
@TypeProperty(name = HEADERS, type = MapOfStringListOfString.class)
@TypeProperty(name = SCHEME, type = String.class)
@TypeProperty(name = METHOD, type = String.class)
public class RESTListenerAttributes extends MessageAttributes {

    public static final String REMOTE_ADDRESS = "remoteAddress";
    public static final String MATCHING_PATH = "matchingPath";
    public static final String QUERY_PARAMS = "queryParams";
    public static final String REQUEST_PATH = "requestPath";
    public static final String REQUEST_URI = "requestUri";
    public static final String QUERY_STRING = "queryString";
    public static final String PATH_PARAMS = "pathParams";
    public static final String VERSION = "version";
    public static final String HEADERS = "headers";
    public static final String SCHEME = "scheme";
    public static final String METHOD = "method";

    public RESTListenerAttributes(HttpRequestWrapper request, String matchingPath) {
        put(MATCHING_PATH, matchingPath);
        put(METHOD, request.method());
        put(SCHEME, request.scheme());
        put(HEADERS, request.headers());
        put(VERSION, request.version());
        put(PATH_PARAMS, request.params());
        put(REQUEST_URI, request.requestUri());
        put(REQUEST_PATH, request.requestPath());
        put(QUERY_PARAMS, request.queryParams());
        put(QUERY_STRING, request.queryString());
        put(REMOTE_ADDRESS, request.remoteAddress());

        // We must set the correlation ID in the Attributes if X-Correlation-ID header is
        // present in the Request Headers, so that the Flow context can use it to set
        // the 'correlationId' context variable available in each flow execution instance.
        if (request.headers().containsKey(HttpHeader.X_CORRELATION_ID)) {
            put(CORRELATION_ID, request.headers().get(HttpHeader.X_CORRELATION_ID).get(0));
        }
    }
}
