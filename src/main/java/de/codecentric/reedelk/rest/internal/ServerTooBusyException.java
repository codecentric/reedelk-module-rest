package de.codecentric.reedelk.rest.internal;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class ServerTooBusyException extends PlatformException {

    public ServerTooBusyException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
