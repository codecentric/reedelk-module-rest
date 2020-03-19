package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.HttpHeader;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.component.listener.OpenApiResponseDefinition;
import com.reedelk.rest.openapi.paths.*;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.runtime.api.message.content.MimeType;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Map;
import java.util.Optional;

import static com.reedelk.runtime.api.commons.StreamUtils.FromString;

public class OpenAPIRequestHandler implements HttpRequestHandler {

    protected OpenAPI openAPI;

    protected OpenAPIRequestHandler() {
        openAPI = new OpenAPI();
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        JSONObject serialize = openAPI.serialize();
        String apiAsJson = serialize.toString(2);
        response.addHeader(HttpHeader.CONTENT_TYPE, MimeType.APPLICATION_JSON.toString());
        response.addHeader(HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        return response.sendByteArray(Mono.just(apiAsJson.getBytes()));
    }

    public void add(String path, RestMethod method, OpenApiConfiguration openApiConfiguration) {

        Boolean excludeApiPath = Optional.ofNullable(openApiConfiguration)
                .flatMap(config -> Optional.ofNullable(config.getExclude()))
                .orElse(false);

        if (!excludeApiPath && openApiConfiguration == null) {
            // Just add the path and the method
            OperationObject operationObject = new OperationObject();
            addOperationObject(method, pathItemObjectFrom(path), operationObject);
            return;
        }

        // If the 'exclude' property is false, we don't add the path, otherwise
        // we add the path to the open API specification.
        if (!excludeApiPath) {

            PathItemObject pathItemObject = pathItemObjectFrom(path);

            Map<String, OpenApiResponseDefinition> responses = openApiConfiguration.getResponse().getResponses();

            responses.forEach((statusCode, openApiResponse) -> {

                MediaTypeObject mediaTypeObject = new MediaTypeObject();
                Optional.ofNullable(openApiResponse.getExample())
                        .ifPresent(resourceText -> mediaTypeObject.setExample(FromString.consume(resourceText.data())));

                String mediaType = openApiResponse.getMediaType();
                ResponseObject responseObject = new ResponseObject();
                responseObject.add(mediaType, mediaTypeObject);

                ResponsesObject responsesObject = new ResponsesObject();
                responsesObject.add(statusCode, responseObject);

                OperationObject operationObject = new OperationObject();
                operationObject.setResponses(responsesObject);

                addOperationObject(method, pathItemObject, operationObject);
            });
        }
    }

    private void addOperationObject(RestMethod method, PathItemObject pathItemObject, OperationObject operationObject) {
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

    private PathItemObject pathItemObjectFrom(String path) {
        Paths paths = openAPI.getPaths();

        // For each Path, you might have /mypath -> POST, GET and so on...
        PathItemObject pathItemObject;
        if (paths.contains(path)) {
            pathItemObject = paths.get(path);
        } else {
            pathItemObject = new PathItemObject();
            paths.add(path, pathItemObject);
        }
        return pathItemObject;
    }

    public void remove(String path, RestMethod method) {
        // TODO: Remove path and method
    }
}
