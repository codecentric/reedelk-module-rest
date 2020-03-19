package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum ParameterLocation {
    @DisplayName("Query")
    QUERY,
    @DisplayName("Header")
    HEADER,
    @DisplayName("Path")
    PATH,
    @DisplayName("Cookie")
    COOKIE
}
