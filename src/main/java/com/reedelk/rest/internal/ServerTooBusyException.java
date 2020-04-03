package com.reedelk.rest.internal;

import com.reedelk.runtime.api.exception.ESBException;

public class ServerTooBusyException extends ESBException {

    public ServerTooBusyException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}
