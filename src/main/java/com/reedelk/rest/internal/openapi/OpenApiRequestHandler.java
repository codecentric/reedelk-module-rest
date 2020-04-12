package com.reedelk.rest.internal.openapi;

import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.OpenApiObject;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.component.listener.openapi.PathsObject;
import com.reedelk.rest.internal.server.HttpRequestHandler;
import com.reedelk.runtime.api.message.content.MimeType;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Optional;

import static com.reedelk.rest.internal.commons.HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.reedelk.rest.internal.commons.HttpHeader.CONTENT_TYPE;

public class OpenApiRequestHandler implements HttpRequestHandler {

    protected OpenApiObject openAPI;
    protected OpenApiSerializableContext context;

    protected OpenApiRequestHandler(RESTListenerConfiguration configuration) {
        openAPI = configuration.getOpenApi();
        openAPI.setBasePath(configuration.getBasePath());
        context = new OpenApiSerializableContext(openAPI.getComponents());
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        String openApiAsJson = serializeOpenApi();
        response.addHeader(CONTENT_TYPE, MimeType.APPLICATION_JSON.toString());
        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(openApiAsJson.getBytes()));
    }

    public void add(String path,
                    RestMethod method,
                    Response response,
                    ErrorResponse errorResponse,
                    OperationObject operationObject) {

        Boolean excludeApiPath = Optional.ofNullable(operationObject)
                .flatMap(config -> Optional.ofNullable(config.getExclude()))
                .orElse(false);

        // This is the default behaviour. If the open api configuration is not present,
        // we just add the path to the open api specification.
        if (operationObject == null) {
            PathsObject paths = openAPI.getPaths();
            paths.add(path, method, response, errorResponse);

            // If the 'exclude' property is false, we don't add the path, otherwise
            // we add the path to the open API specification.
        } else if (!excludeApiPath) {
            PathsObject paths = openAPI.getPaths();
            paths.add(path, method, response, errorResponse, operationObject);
        }
    }

    public void remove(String path, RestMethod method) {
        PathsObject paths = openAPI.getPaths();
        paths.remove(path, method);
    }

    String serializeOpenApi() {
        JSONObject serialize = openAPI.serialize(context);
        return serialize.toString(2);
    }
}
