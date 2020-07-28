package com.reedelk.rest.internal.openapi;

import com.reedelk.openapi.v3.OpenApiObjectAbstract;
import com.reedelk.rest.internal.commons.HttpContentType;
import com.reedelk.runtime.api.message.content.MimeType;

public enum Formatter {

    JSON {
        @Override
        String format(OpenApiObjectAbstract openApiObject,
                      OpenApiSerializableContext context) {
            return openApiObject.toJson(context);
        }

        @Override
        String contentType() {
            return MimeType.APPLICATION_JSON.toString();
        }
    },

    YAML {
        @Override
        String format(OpenApiObjectAbstract openApiObject,
                      OpenApiSerializableContext context) {
            return openApiObject.toYaml(context);
        }

        @Override
        String contentType() {
            return HttpContentType.YAML_CONTENT_TYPE;
        }
    };

    abstract String format(OpenApiObjectAbstract openApiObject,
                           OpenApiSerializableContext context);

    abstract String contentType();
}
