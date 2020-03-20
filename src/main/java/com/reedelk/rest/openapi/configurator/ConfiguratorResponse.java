package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.paths.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.commons.StringUtils;

import java.util.Optional;

public class ConfiguratorResponse extends AbstractConfigurator {

    @Override
    public void configure(OpenAPI api, RestMethod method, OpenApiConfiguration configuration, OperationObject operationObject) {
        Optional.ofNullable(configuration.getResponse()).ifPresent(response -> {

            ResponsesObject responsesObject = new ResponsesObject();
            operationObject.setResponses(responsesObject);

            response.getResponses().forEach((statusCode, openApiResponse) -> {
                MediaTypeObject mediaTypeObject = new MediaTypeObject();

                Optional.ofNullable(openApiResponse.getExample()).ifPresent(resourceText ->
                        mediaTypeObject.setExample(StreamUtils.FromString.consume(resourceText.data())));

                Optional.ofNullable(openApiResponse.getSchema()).ifPresent(resourceText -> {
                    String schemaRef = schemaRefFrom(api, resourceText);
                    ReferenceObject referenceObject = new ReferenceObject();
                    referenceObject.set$ref(schemaRef);
                    mediaTypeObject.setSchema(referenceObject);
                });

                String mediaType = openApiResponse.getMediaType();
                ResponseObject responseObject = new ResponseObject();
                responseObject.setDescription(Optional.ofNullable(openApiResponse.getDescription()).orElse(StringUtils.EMPTY));
                responseObject.add(mediaType, mediaTypeObject);

                responsesObject.add(statusCode, responseObject);
            });
        });
    }
}
