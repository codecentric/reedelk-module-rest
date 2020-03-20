package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum ParameterLocation {
    @DisplayName("Query")
    QUERY("query"),
    @DisplayName("Header")
    HEADER("header"),
    @DisplayName("Path")
    PATH("path"),
    @DisplayName("Cookie")
    COOKIE("cookie");

    private final String value;

    ParameterLocation(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
