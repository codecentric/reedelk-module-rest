package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.paths.MediaTypeObject;
import com.reedelk.rest.openapi.paths.ReferenceObject;
import com.reedelk.rest.openapi.paths.RequestBodyObject;
import com.reedelk.runtime.api.commons.StreamUtils;

import java.util.Optional;

public class ConfiguratorRequest extends AbstractConfigurator {

    @Override
    public void configure(OpenAPI api, RestMethod method, OperationObject configuration, com.reedelk.rest.openapi.paths.OperationObject operationObject) {
        if (RestMethod.GET.equals(method)) {
            return;
        }

        Optional.ofNullable(configuration.getRequestBody()).ifPresent(request -> {

            RequestBodyObject requestBodyObject = new RequestBodyObject();
            requestBodyObject.setDescription(request.getDescription());
            requestBodyObject.setRequired(request.getRequired());
            operationObject.setRequestBody(requestBodyObject);

            request.getContent().forEach((mediaType, openApiRequestDefinition) -> {

                MediaTypeObject mediaTypeObject = new MediaTypeObject();
                Optional.ofNullable(openApiRequestDefinition.getExample()).ifPresent(resourceText ->
                        mediaTypeObject.setExample(StreamUtils.FromString.consume(resourceText.data())));

                Optional.ofNullable(openApiRequestDefinition.getSchema()).ifPresent(resourceText -> {
                    String schemaRef = schemaRefFrom(api, resourceText);
                    ReferenceObject referenceObject = new ReferenceObject();
                    referenceObject.set$ref(schemaRef);
                    mediaTypeObject.setSchema(referenceObject);
                });

                requestBodyObject.add(mediaType, mediaTypeObject);
            });
        });
    }
}
