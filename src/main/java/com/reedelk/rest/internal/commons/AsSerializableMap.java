package com.reedelk.rest.internal.commons;

import java.util.HashMap;
import java.util.Map;

public class AsSerializableMap {

    private AsSerializableMap() {
    }

    public static <ValueType> HashMap<String, ValueType> of(Map<String, ValueType> original) {
        return original == null ?
                new HashMap<>() :
                new HashMap<>(original);
    }
}
