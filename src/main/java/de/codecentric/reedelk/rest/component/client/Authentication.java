package de.codecentric.reedelk.rest.component.client;

import de.codecentric.reedelk.runtime.api.annotation.DisplayName;

public enum Authentication {
    @DisplayName("None")
    NONE,
    @DisplayName("Basic")
    BASIC,
    @DisplayName("Digest")
    DIGEST
}
