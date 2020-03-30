package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.Defaults;
import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.RestListenerConfiguration;
import com.reedelk.rest.component.listener.openapi.OpenApiObject;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.component.listener.openapi.PathsObject;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.content.MimeType;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.reedelk.rest.commons.HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.reedelk.rest.commons.HttpHeader.CONTENT_TYPE;

public class OpenAPIRequestHandler implements HttpRequestHandler {

    protected OpenApiObject openAPI;

    protected OpenAPIRequestHandler(RestListenerConfiguration configuration) {
        openAPI = configuration.getOpenApi();
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        String openApiAsJson = serializeOpenApi();
        response.addHeader(CONTENT_TYPE, MimeType.APPLICATION_JSON.toString());
        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(openApiAsJson.getBytes()));
    }

    public void add(String path, RestMethod method, OperationObject operationObject) {

        Boolean excludeApiPath = Optional.ofNullable(operationObject)
                .flatMap(config -> Optional.ofNullable(config.getExclude()))
                .orElse(false);

        // This is the default behaviour. If the open api configuration is not present,
        // we just add the path to the open api specification.
        if (operationObject == null) {
            PathsObject paths = openAPI.getPaths();
            paths.add(path, method);

            // If the 'exclude' property is false, we don't add the path, otherwise
            // we add the path to the open API specification.
        } else if (!excludeApiPath) {
            PathsObject paths = openAPI.getPaths();
            paths.add(path, method, operationObject);
        }
    }

    public void remove(String path, RestMethod method) {

    }

    String serializeOpenApi() {
        JSONObject serialize = openAPI.serialize();
        return serialize.toString(2);
    }




    private String defaultServerURL(RestListenerConfiguration configuration) {
        HttpProtocol protocol = configuration.getProtocol();
        String host = Defaults.RestListener.host(configuration.getHost());
        int port = Defaults.RestListener.port(configuration.getPort(), protocol);
        try {
            return new URL(protocol.name(), host, port, configuration.getBasePath()).toString();
        } catch (MalformedURLException exception) {
            throw new ESBException(exception);
        }
    }
}
