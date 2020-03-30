package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.reedelk.runtime.api.commons.StreamUtils.FromString;

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

    default void set(JSONObject object, String propertyName, List<? extends OpenApiSerializable> serializableList) {
        if (serializableList != null && !serializableList.isEmpty()) {
            JSONArray array = new JSONArray();
            serializableList.forEach(serializable -> array.put(serializable.serialize()));
            object.put(propertyName, array);
        }
    }

    default void set(JSONObject object, String propertyName, Map<String, ? extends OpenApiSerializable> serializableMap) {
        if (serializableMap != null && !serializableMap.isEmpty()) {
            JSONObject serializedMapObject = JsonObjectFactory.newJSONObject();
            serializableMap.forEach((key, mapObject) -> set(serializedMapObject, key, mapObject));
            set(object, propertyName, serializedMapObject);
        }
    }

    default void setList(JSONObject object, String propertyName, List<String> items) {
        if (items != null && !items.isEmpty()) {
            object.put(propertyName, items);
        }
    }

    default void set(JSONObject object, String propertyName, String value) {
        if (value != null) {
            object.put(propertyName, value);
        }
    }

    default void set(JSONObject object, String propertyName, ResourceText resource) {
        if (resource != null) {
            object.put(propertyName, FromString.consume(resource.data()));
        }
    }
}
