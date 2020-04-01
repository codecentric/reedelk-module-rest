package com.reedelk.rest.commons;

import com.reedelk.runtime.api.commons.FileUtils;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.commons.StringUtils;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;

import static com.reedelk.runtime.api.commons.Preconditions.checkArgument;

public class SchemaId {

    public static String from(ResourceText schema) {
        String schemaAsJson = StreamUtils.FromString.consume(schema.data());
        JSONObject schemaAsJsonObject = new JSONObject(schemaAsJson);

        String schemaId;
        if (schemaAsJsonObject.has("title")) {
            String titleProperty = schemaAsJsonObject.getString("title");
            schemaId = SchemaId.normalizeNameFrom(titleProperty);
        } else if (schemaAsJsonObject.has("name")) {
            String nameProperty = schemaAsJsonObject.getString("name");
            schemaId = SchemaId.normalizeNameFrom(nameProperty);
        } else {
            String path = schema.path();
            schemaId = SchemaId.fromFilePath(path);
        }

        return schemaId;
    }

    private static String fromFilePath(String path) {
        checkArgument(StringUtils.isNotBlank(path), "path");

        String fileName = path.substring(path.lastIndexOf('/') + 1);
        String fileNameWithoutExtension = FileUtils.removeExtension(fileName);

        // If file named Person.schema.json -> Person
        if (fileNameWithoutExtension.endsWith(".schema")) {
            fileNameWithoutExtension = FileUtils.removeExtension(fileNameWithoutExtension);
        }

        return normalizeNameFrom(fileNameWithoutExtension);
    }

    /**
     * A function which removes white spaces, dots, hyphens
     * and other not desired character when the schema name
     * is taken from file name.
     *
     */
    private static String normalizeNameFrom(String value) {
        checkArgument(StringUtils.isNotBlank(value), "value");
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }
}
