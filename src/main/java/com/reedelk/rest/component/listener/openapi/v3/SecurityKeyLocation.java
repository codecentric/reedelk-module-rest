package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum SecurityKeyLocation {

    @DisplayName("Query")
    query,
    @DisplayName("Header")
    header,
    @DisplayName("Cookie")
    cookie
}
