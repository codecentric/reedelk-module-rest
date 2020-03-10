package com.reedelk.rest.commons;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class JsonObjectFactory {

    private static final Logger LOG = LoggerFactory.getLogger(JsonObjectFactory.class);

    private static Field jsonObjectMapField;

    static {
        try {
            jsonObjectMapField = JSONObject.class.getDeclaredField("map");
            jsonObjectMapField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LOG.error("Could not find JSON Object field 'map'", e);
        }
    }

    private JsonObjectFactory() {
    }

    /**
     * Instantiates a new JSON Object with an ordered HashMap. This
     * is needed so that properties are serialized in the same order
     * they are added during the serialization process.
     *
     * @return a new JSONObject instance with an internal representation
     * which keeps the order in which properties are added to the Object.
     */
    public static JSONObject newJSONObject() {
        JSONObject result = new JSONObject();
        if (jsonObjectMapField != null) {
            try {
                jsonObjectMapField.set(result, new LinkedHashMap<>());
            } catch (IllegalAccessException e) {
                LOG.error("Could not configure ordered map to JSONObject", e);
            }
        }
        return result;
    }
}
