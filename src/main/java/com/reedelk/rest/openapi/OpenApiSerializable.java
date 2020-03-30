package com.reedelk.rest.openapi;

import org.json.JSONObject;

import java.util.List;

public interface OpenApiSerializable {

    JSONObject serialize();

    default void set(JSONObject object, String propertyName, OpenApiSerializable serializable) {
        if (serializable != null) {
            object.put(propertyName, serializable.serialize());
        }
    }

    default void set(JSONObject object, String propertyName, JSONObject value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }

    default void set(JSONObject object, String propertyName, List<String> items) {
        if (items != null && !items.isEmpty()) {
            object.put(propertyName, items);
        }
    }

    default void set(JSONObject object, String propertyName, String value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }
}
