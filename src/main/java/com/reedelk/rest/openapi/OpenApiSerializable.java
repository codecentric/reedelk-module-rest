package com.reedelk.rest.openapi;

import org.json.JSONObject;

public interface OpenApiSerializable {

    JSONObject serialize();

    default void set(JSONObject object, String propertyName, OpenApiSerializable serializable) {
        if (serializable != null) {
            object.put(propertyName, serializable.serialize());
        }
    }

    default void set(JSONObject object, String propertyName, String value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }
}
