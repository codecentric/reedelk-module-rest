package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.DisplayName;

import java.util.Map;

public enum PredefinedSchema {

    @DisplayName("Custom Schema")
    NONE(null),
    @DisplayName("String")
    STRING(com.reedelk.openapi.commons.PredefinedSchema.STRING),
    @DisplayName("Integer")
    INTEGER(com.reedelk.openapi.commons.PredefinedSchema.INTEGER),
    @DisplayName("Array of Integers")
    ARRAY_INTEGER(com.reedelk.openapi.commons.PredefinedSchema.ARRAY_INTEGER),
    @DisplayName("Array of Strings")
    ARRAY_STRING(com.reedelk.openapi.commons.PredefinedSchema.ARRAY_STRING);

    private com.reedelk.openapi.commons.PredefinedSchema schema;

    PredefinedSchema(com.reedelk.openapi.commons.PredefinedSchema schema) {
        this.schema = schema;
    }

    public Map<String, Object> schema() {
        return schema.schema();
    }
}
