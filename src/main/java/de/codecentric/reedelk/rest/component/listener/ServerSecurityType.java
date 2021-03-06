package de.codecentric.reedelk.rest.component.listener;

import de.codecentric.reedelk.runtime.api.annotation.DisplayName;

public enum ServerSecurityType {
    @DisplayName("Certificate and private key")
    CERTIFICATE_AND_PRIVATE_KEY,
    @DisplayName("Key store")
    KEY_STORE
}
