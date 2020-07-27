package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.commons.FileUtils;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.v3.model.SchemaReference;
import org.json.JSONObject;

public class SchemaUtils {

    public static SchemaReference toSchemaReference(ResourceText resourceText) {
        // Schema Data could be JSON or YAML.
        String schemaData = StreamUtils.FromString.consume(resourceText.data());

        // Extract schema id from JSON could be YAML ?
        String schemaId;
        JSONObject schemaAsJsonObject = new JSONObject(schemaData);
        if (schemaAsJsonObject.has("title")) {
            String titleProperty = schemaAsJsonObject.getString("title");
            schemaId = normalizeNameFrom(titleProperty);
        } else if (schemaAsJsonObject.has("name")) {
            String nameProperty = schemaAsJsonObject.getString("name");
            schemaId = normalizeNameFrom(nameProperty);
        } else {
            String path = resourceText.path();
            schemaId = fromFilePath(path);
        }

        return new SchemaReference(schemaId, schemaData);
    }

    private static String fromFilePath(String path) {
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
        //  checkArgument(StringUtils.isNotBlank(value), "value");
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }
}
