package de.codecentric.reedelk.rest.internal;

import de.codecentric.reedelk.runtime.api.exception.PlatformException;

public class ExecutionException extends PlatformException {

    public ExecutionException(String errorMessage) {
        super(errorMessage);
    }

    public ExecutionException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
