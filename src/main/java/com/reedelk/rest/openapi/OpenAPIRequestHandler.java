package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.Defaults;
import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.RestListenerConfiguration;
import com.reedelk.rest.component.listener.OpenApiBaseConfiguration;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.component.listener.OpenApiResponse;
import com.reedelk.rest.openapi.configurator.OpenApiConfigurator;
import com.reedelk.rest.openapi.info.InfoObject;
import com.reedelk.rest.openapi.paths.OperationObject;
import com.reedelk.rest.openapi.paths.PathItemObject;
import com.reedelk.rest.openapi.paths.Paths;
import com.reedelk.rest.openapi.server.ServerObject;
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
import java.util.List;
import java.util.Optional;

import static com.reedelk.rest.commons.HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.reedelk.rest.commons.HttpHeader.CONTENT_TYPE;

public class OpenAPIRequestHandler implements HttpRequestHandler {

    protected OpenAPI openAPI;

    protected OpenAPIRequestHandler(RestListenerConfiguration configuration) {
        openAPI = new OpenAPI();

        OpenApiBaseConfiguration openApiConfiguration = configuration.getOpenApiConfiguration();
        Optional.ofNullable(openApiConfiguration).ifPresent(openApiBaseConfiguration -> {
            InfoObject info = openAPI.getInfo();
            info.setTitle(openApiBaseConfiguration.getInfo().getTitle());
            info.setDescription(openApiBaseConfiguration.getInfo().getDescription());
            info.setVersion(openApiBaseConfiguration.getInfo().getVersion());
        });

        Boolean configHasServers = Optional.ofNullable(openApiConfiguration)
                .map(openApiBaseConfiguration -> !openApiBaseConfiguration.getServers().isEmpty())
                .orElse(false);

        if (!configHasServers) {
            ServerObject serverObject = new ServerObject();
            serverObject.setUrl(defaultServerURL(configuration));
            List<ServerObject> servers = openAPI.getServers();
            servers.add(serverObject);
        }
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        JSONObject serialize = openAPI.serialize();
        String apiAsJson = serialize.toString(2);
        response.addHeader(CONTENT_TYPE, MimeType.APPLICATION_JSON.toString());
        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(apiAsJson.getBytes()));
    }

    public void add(String path, RestMethod method, OpenApiConfiguration openApiConfiguration) {

        Boolean excludeApiPath = Optional.ofNullable(openApiConfiguration)
                .flatMap(config -> Optional.ofNullable(config.getExclude()))
                .orElse(false);

        // This is the default behaviour. If the open api configuration is not present,
        // we just add the path to the open api specification.
        if (openApiConfiguration == null) {
            addOperationFrom(path, method);

            // If the 'exclude' property is false, we don't add the path, otherwise
            // we add the path to the open API specification.
        } else if (!excludeApiPath) {
            addOperationFrom(path, method, openApiConfiguration);
        }
    }

    public void remove(String path, RestMethod method) {
        PathItemObject pathItemObject = pathItemObjectFrom(path);
        setOperationObject(method, pathItemObject, null);
    }

    private void addOperationFrom(String path, RestMethod method, OpenApiConfiguration openApiConfiguration) {
        OpenApiResponse response = openApiConfiguration.getResponse();

        PathItemObject pathItemObject = pathItemObjectFrom(path);
        pathItemObject.setDescription(response.getDescription());

        OperationObject operationObject = OpenApiConfigurator.configure(openAPI, method, openApiConfiguration);
        setOperationObject(method, pathItemObject, operationObject);
    }

    private void setOperationObject(RestMethod method, PathItemObject pathItemObject, OperationObject operationObject) {
        if (RestMethod.GET.equals(method)) {
            pathItemObject.setGet(operationObject);
        } else if (RestMethod.POST.equals(method)) {
            pathItemObject.setPost(operationObject);
        } else if (RestMethod.PUT.equals(method)) {
            pathItemObject.setPut(operationObject);
        } else if (RestMethod.DELETE.equals(method)) {
            pathItemObject.setDelete(operationObject);
        } else if (RestMethod.HEAD.equals(method)) {
            pathItemObject.setHead(operationObject);
        } else if (RestMethod.OPTIONS.equals(method)) {
            pathItemObject.setOptions(operationObject);
        } else {
            throw new IllegalArgumentException(method.name());
        }
    }

    private void addOperationFrom(String path, RestMethod method) {
        OperationObject operationObject = new OperationObject();
        setOperationObject(method, pathItemObjectFrom(path), operationObject);
    }

    private PathItemObject pathItemObjectFrom(String path) {
        Paths paths = openAPI.getPaths();

        // For each Path, you might have /mypath -> POST, GET and so on...
        PathItemObject pathItemObject;
        if (path != null && paths.contains(path)) {
            pathItemObject = paths.get(path);
        } else {
            pathItemObject = new PathItemObject();
            paths.add(path, pathItemObject);
        }
        return pathItemObject;
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
