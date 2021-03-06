package de.codecentric.reedelk.rest.internal.commons;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;

public enum HttpProtocol {

    HTTP,
    HTTPS;

    public static HttpProtocol from(String protocol) {
        return StringUtils.isBlank(protocol) ?
                HttpProtocol.HTTP :
                HttpProtocol.valueOf(protocol.toUpperCase());
    }
}
