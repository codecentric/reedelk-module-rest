package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.openapi.paths.*;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.message.content.MimeType;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.reedelk.rest.commons.HttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN;
import static com.reedelk.rest.commons.HttpHeader.CONTENT_TYPE;
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
        // TODO: Remove path and method
    }

    private void addOperationFrom(String path, RestMethod method, OpenApiConfiguration openApiConfiguration) {
        PathItemObject pathItemObject = pathItemObjectFrom(path);

        OperationObject operationObject = new OperationObject();
        operationObject.setOperationId(openApiConfiguration.getOperationId());
        operationObject.setDescription(openApiConfiguration.getDescription());
        operationObject.setSummary(openApiConfiguration.getSummary());

        processRequest(openApiConfiguration, operationObject);
        processResponse(openApiConfiguration, operationObject);
        processParameters(openApiConfiguration, operationObject);

        addOperationObject(method, pathItemObject, operationObject);
    }

    private void processParameters(OpenApiConfiguration openApiConfiguration, OperationObject operationObject) {
        Optional.ofNullable(openApiConfiguration.getParameters()).ifPresent(openApiParameters -> {
            List<ParameterObject> parameterObjectList = new ArrayList<>();
            operationObject.setParameters(parameterObjectList);

            openApiParameters.getParameters().forEach((parameterName, openApiParameterDefinition) -> {
                ParameterObject parameterObject = new ParameterObject();
                parameterObject.setDeprecated(openApiParameterDefinition.getDeprecated());
                parameterObject.setDescription(openApiParameterDefinition.getDescription());
                parameterObject.setRequired(openApiParameterDefinition.getRequired());
                parameterObject.setIn(openApiParameterDefinition.getIn().name());
                parameterObject.setName(parameterName);
                parameterObjectList.add(parameterObject);
            });
        });
    }

    private void processRequest(OpenApiConfiguration openApiConfiguration, OperationObject operationObject) {
        Optional.ofNullable(openApiConfiguration.getRequest()).ifPresent(request -> {

            RequestBodyObject requestBodyObject = new RequestBodyObject();
            requestBodyObject.setDescription(request.getDescription());
            requestBodyObject.setRequired(request.getRequired());
            operationObject.setRequestBody(requestBodyObject);

            request.getRequests().forEach((mediaType, openApiRequestDefinition) -> {
                //openApiRequestDefinition.
                MediaTypeObject mediaTypeObject = new MediaTypeObject();
                Optional.ofNullable(openApiRequestDefinition.getExample()).ifPresent(resourceText ->
                        mediaTypeObject.setExample(FromString.consume(resourceText.data())));
                // TODO: Schema
                requestBodyObject.add(mediaType, mediaTypeObject);
            });
        });
    }

    private void processResponse(OpenApiConfiguration openApiConfiguration, OperationObject operationObject) {
        Optional.ofNullable(openApiConfiguration.getResponse()).ifPresent(response -> {

            ResponsesObject responsesObject = new ResponsesObject();
            operationObject.setResponses(responsesObject);

            response.getResponses().forEach((statusCode, openApiResponse) -> {
                MediaTypeObject mediaTypeObject = new MediaTypeObject();
                Optional.ofNullable(openApiResponse.getExample()).ifPresent(resourceText ->
                                mediaTypeObject.setExample(FromString.consume(resourceText.data())));
                // TODO: Schema
                String mediaType = openApiResponse.getMediaType();
                ResponseObject responseObject = new ResponseObject();
                responseObject.setDescription(Optional.ofNullable(openApiResponse.getDescription()).orElse(StringUtils.EMPTY));
                responseObject.add(mediaType, mediaTypeObject);

                responsesObject.add(statusCode, responseObject);
            });
        });
    }

    private void addOperationFrom(String path, RestMethod method) {
        OperationObject operationObject = new OperationObject();
        addOperationObject(method, pathItemObjectFrom(path), operationObject);
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
}
