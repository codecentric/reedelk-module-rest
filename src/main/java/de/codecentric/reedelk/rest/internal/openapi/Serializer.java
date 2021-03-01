package de.codecentric.reedelk.rest.internal.openapi;

import de.codecentric.reedelk.openapi.OpenApi;
import de.codecentric.reedelk.openapi.v3.model.OpenApiObject;
import de.codecentric.reedelk.rest.internal.commons.HttpContentType;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;

public enum Serializer {

    JSON {
        @Override
        String serialize(OpenApiObject openApiObject) {
            return OpenApi.toJson(openApiObject);
        }

        @Override
        String contentType() {
            return MimeType.APPLICATION_JSON.toString();
        }
    },

    YAML {
        @Override
        String serialize(OpenApiObject openApiObject) {
            return OpenApi.toYaml(openApiObject);
        }

        @Override
        String contentType() {
            return HttpContentType.YAML_CONTENT_TYPE;
        }
    };

    abstract String serialize(OpenApiObject openApiObject);

    abstract String contentType();
}
