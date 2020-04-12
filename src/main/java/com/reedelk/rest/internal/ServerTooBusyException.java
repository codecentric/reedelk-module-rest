package com.reedelk.rest.internal;

import com.reedelk.runtime.api.exception.PlatformException;

public class ServerTooBusyException extends PlatformException {

    public ServerTooBusyException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
