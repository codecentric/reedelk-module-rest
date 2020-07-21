package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum ParameterStyle {
    @DisplayName("Matrix")
    matrix,
    @DisplayName("Label")
    label,
    @DisplayName("Form")
    form,
    @DisplayName("Simple")
    simple,
    @DisplayName("Space Delimited")
    spaceDelimited,
    @DisplayName("Pipe Delimited")
    pipeDelimited,
    @DisplayName("Deep Object")
    deepObject
}
