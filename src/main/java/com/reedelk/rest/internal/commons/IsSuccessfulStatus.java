package com.reedelk.rest.internal.commons;

public class IsSuccessfulStatus {

    private IsSuccessfulStatus() {
    }

    public static boolean status(int code) {
        return ((200 <= code) && (code <= 299));
    }
}
