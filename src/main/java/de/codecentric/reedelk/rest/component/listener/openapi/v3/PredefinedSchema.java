package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.runtime.api.annotation.DisplayName;

import java.util.Map;

public enum PredefinedSchema {

    @DisplayName("Custom Schema")
    NONE(null),
    @DisplayName("String")
    STRING(de.codecentric.reedelk.openapi.commons.PredefinedSchema.STRING),
    @DisplayName("Integer")
    INTEGER(de.codecentric.reedelk.openapi.commons.PredefinedSchema.INTEGER),
    @DisplayName("Array of Integers")
    ARRAY_INTEGER(de.codecentric.reedelk.openapi.commons.PredefinedSchema.ARRAY_INTEGER),
    @DisplayName("Array of Strings")
    ARRAY_STRING(de.codecentric.reedelk.openapi.commons.PredefinedSchema.ARRAY_STRING);

    private de.codecentric.reedelk.openapi.commons.PredefinedSchema schema;

    PredefinedSchema(de.codecentric.reedelk.openapi.commons.PredefinedSchema schema) {
        this.schema = schema;
    }

    public Map<String, Object> schema() {
        return schema.schema();
    }
}
