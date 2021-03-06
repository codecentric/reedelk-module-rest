package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.runtime.api.annotation.DisplayName;

public enum SecurityType {

    @DisplayName("API Key")
    apiKey,
    @DisplayName("HTTP")
    http,
    @DisplayName("OAuth2")
    oauth2,
    @DisplayName("OpenID Connect")
    openIdConnect
}
