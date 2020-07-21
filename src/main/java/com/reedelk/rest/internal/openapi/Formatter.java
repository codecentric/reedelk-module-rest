package com.reedelk.rest.internal.openapi;

import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;

public enum Formatter {

    JSON {
        @Override
        String format(com.reedelk.runtime.openapi.v3.model.OpenApiObject openApiObject,
                      OpenApiSerializableContext context) {
            return openApiObject.toJson(context);
        }
    },
    YAML {
        @Override
        String format(com.reedelk.runtime.openapi.v3.model.OpenApiObject openApiObject,
                      OpenApiSerializableContext context) {
            return openApiObject.toYaml(context);
        }
    };

    abstract String format(com.reedelk.runtime.openapi.v3.model.OpenApiObject openApiObject,
                           OpenApiSerializableContext context);
}
