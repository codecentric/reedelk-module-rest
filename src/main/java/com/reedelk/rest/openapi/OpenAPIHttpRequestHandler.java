package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.HttpHeader;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.openapi.paths.*;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.runtime.api.message.content.MimeType;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class OpenAPIHttpRequestHandler implements HttpRequestHandler {

    private OpenAPI openAPI;

    public OpenAPIHttpRequestHandler() {
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

    public void add(String path, RestMethod method) {
        Paths paths = openAPI.getPaths();
        if (paths == null) {
            paths = new Paths();
            openAPI.setPaths(paths);
        }
        boolean contains = paths.contains(path);

        PathItemObject pathItemObject;
        if (contains) {
            pathItemObject = paths.get(path);
        } else {
            pathItemObject = new PathItemObject();
            paths.add(path, pathItemObject);
        }

        MediaTypeObject mediaTypeObject = new MediaTypeObject();
        ResponseObject responseObject = new ResponseObject();
        responseObject.add(MimeType.APPLICATION_JSON.toString(), mediaTypeObject);
        OperationObject operationObject = new OperationObject();

        ResponsesObject responsesObject = new ResponsesObject();
        responsesObject.add("200", responseObject);
        operationObject.setResponses(responsesObject);

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
}
