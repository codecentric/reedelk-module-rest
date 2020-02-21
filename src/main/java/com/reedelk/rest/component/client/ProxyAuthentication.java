package com.reedelk.rest.component.client;

import com.reedelk.runtime.api.annotation.DisplayName;

public enum ProxyAuthentication {
    @DisplayName("None")
    NONE,
    @DisplayName("Basic")
    BASIC,
    @DisplayName("Digest")
    DIGEST
}
