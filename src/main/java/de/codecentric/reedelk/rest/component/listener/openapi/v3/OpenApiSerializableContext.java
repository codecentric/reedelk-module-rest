package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.openapi.v3.model.ExampleObject;
import de.codecentric.reedelk.openapi.v3.model.Schema;
import de.codecentric.reedelk.runtime.api.commons.FileUtils;
import de.codecentric.reedelk.runtime.api.commons.StreamUtils;
import de.codecentric.reedelk.runtime.api.resource.ResourceText;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OpenApiSerializableContext {

    private static final String SCHEMA_REFERENCE_TEMPLATE = "#/components/schemas/%s";
    private static final String EXAMPLE_REFERENCE_TEMPLATE = "#/components/examples/%s";

    private final Map<String, SchemaDataHolder> schemasMap = new HashMap<>();
    private final Map<String, ExampleDataHolder> examplesMap = new HashMap<>();

    public Map<String, Schema> getSchemas() {
        Map<String, Schema> result = new HashMap<>();
        schemasMap.forEach((resourcePath, schemaDataHolder) ->
                result.put(schemaDataHolder.schemaId, new Schema(schemaDataHolder.schemaData)));
        return result;
    }

    public Map<String, ExampleObject> getExamples() {
        Map<String, ExampleObject> result = new HashMap<>();
        examplesMap.forEach((examplePath, exampleDataHolder) -> {
            result.put(exampleDataHolder.exampleId, exampleDataHolder.exampleObject);
        });
        return result;
    }

    /**
     * This is a user defined schema with the given ID.
     */
    public Schema register(String userDefinedId, ResourceText schemaResource) {
        Map<String, Object> schemaDataAsMap = schemaDataFrom(schemaResource);
        SchemaDataHolder schemaDataHolder = new SchemaDataHolder(userDefinedId, schemaDataAsMap);
        schemasMap.put(schemaResource.path(), schemaDataHolder);
        return new Schema(schemaDataAsMap);
    }

    public Schema getSchema(ResourceText schemaResource, Boolean inlineSchema) {
        boolean shouldInline = Optional.ofNullable(inlineSchema).orElse(false);
        return shouldInline ? getInlineSchema(schemaResource) : getSchema(schemaResource);
    }

    public Schema getSchema(PredefinedSchema predefinedSchema, ResourceText resourceSchema, Boolean inlineSchema) {
        if (PredefinedSchema.NONE.equals(predefinedSchema) && resourceSchema != null) {
            return getSchema(resourceSchema, inlineSchema);
        }
        if (!PredefinedSchema.NONE.equals(predefinedSchema)) {
            // Immediately build the schema inline.
            return new Schema(predefinedSchema.schema());
        }
        return null;
    }

    public void registerExample(String exampleId, ResourceText resourceText, ExampleObject exampleResource) {
        ExampleDataHolder schemaDataHolder = new ExampleDataHolder(exampleId, exampleResource);
        examplesMap.put(resourceText.path(), schemaDataHolder);
    }

    public ExampleObject getExampleObject(ResourceText exampleResource) {
        // If exists a user defined, then use that ID, otherwise generate one.
        if (examplesMap.containsKey(exampleResource.path())) {
            ExampleDataHolder holder = examplesMap.get(exampleResource.path());
            ExampleObject exampleObject = new ExampleObject();
            exampleObject.setExampleRef(formatExampleReference(holder));
            return exampleObject;
        } else {
            String exampleData = exampleDataFrom(exampleResource);
            String exampleId = generateExampleId(exampleResource);
            ExampleObject exampleObject = new ExampleObject();
            exampleObject.setValue(exampleData);

            ExampleDataHolder holder = new ExampleDataHolder(exampleId, exampleObject);
            examplesMap.put(exampleResource.path(), holder);
            ExampleObject objectWithRef = new ExampleObject();
            objectWithRef.setExampleRef(formatExampleReference(holder));
            return objectWithRef;
        }
    }

    private String fromFilePath(String path) {
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        String fileNameWithoutExtension = FileUtils.removeExtension(fileName);
        // If file named Person.schema.json -> Person
        if (fileNameWithoutExtension.endsWith(".schema")) {
            fileNameWithoutExtension = FileUtils.removeExtension(fileNameWithoutExtension);
        }
        return normalizeNameFrom(fileNameWithoutExtension);
    }

    private Schema getSchema(ResourceText schemaResource) {
        // If exists a user defined, then use that ID, otherwise generate one.
        if (schemasMap.containsKey(schemaResource.path())) {
            SchemaDataHolder schemaDataHolder = schemasMap.get(schemaResource.path());
            return new Schema(formatSchemaReference(schemaDataHolder));
        } else {
            Map<String, Object> schemaDataAsMap = schemaDataFrom(schemaResource);
            String schemaGeneratedId = generateSchemaId(schemaDataAsMap, schemaResource);
            SchemaDataHolder schemaDataHolder = new SchemaDataHolder(schemaGeneratedId, schemaDataAsMap);
            schemasMap.put(schemaResource.path(), schemaDataHolder);
            return new Schema(formatSchemaReference(schemaDataHolder));
        }
    }

    private Schema getInlineSchema(ResourceText schemaResource) {
        Map<String, Object> schemaDataAsMap = schemaDataFrom(schemaResource);
        return new Schema(schemaDataAsMap);
    }

    /**
     * Extract schema id from JSON could be YAML
     */
    private String generateSchemaId(Map<String, Object> schemaDataAsMap, ResourceText schemaResource) {
        if (schemaDataAsMap.containsKey("title")) {
            String titleProperty = (String) schemaDataAsMap.get("title");
            return normalizeNameFrom(titleProperty);
        } else if (schemaDataAsMap.containsKey("name")) {
            String nameProperty = (String) schemaDataAsMap.get("name");
            return normalizeNameFrom(nameProperty);
        } else {
            String path = schemaResource.path();
            return fromFilePath(path);
        }
    }

    /**
     * Extract schema id from JSON could be YAML
     */
    private String generateExampleId(ResourceText exampleResource) {
        String path = exampleResource.path();
        return fromFilePath(path);
    }

    private Map<String, Object> schemaDataFrom(ResourceText schemaResource) {
        String schemaDataAsString = StreamUtils.FromString.consume(schemaResource.data());
        Yaml yaml = new Yaml();
        return yaml.load(schemaDataAsString); // TODO: Handle this null
    }

    private String exampleDataFrom(ResourceText schemaResource) {
        return StreamUtils.FromString.consume(schemaResource.data());
    }

    private String formatSchemaReference(SchemaDataHolder schemaDataHolder) {
        return String.format(SCHEMA_REFERENCE_TEMPLATE, schemaDataHolder.schemaId);
    }

    private String formatExampleReference(ExampleDataHolder exampleDataHolder) {
        return String.format(EXAMPLE_REFERENCE_TEMPLATE, exampleDataHolder.exampleId);
    }

    /**
     * A function which removes white spaces, dots, hyphens
     * and other not desired character when the schema name
     * is taken from file name.
     */
    private String normalizeNameFrom(String value) {
        //  checkArgument(StringUtils.isNotBlank(value), "value");
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }

    static class SchemaDataHolder {

        final String schemaId;
        final Map<String, Object> schemaData;

        SchemaDataHolder(String schemaId, Map<String, Object> schemaData) {
            this.schemaId = schemaId;
            this.schemaData = schemaData;
        }
    }

    static class ExampleDataHolder {

        final String exampleId;
        final ExampleObject exampleObject;

        ExampleDataHolder(String exampleId, ExampleObject exampleObject) {
            this.exampleId = exampleId;
            this.exampleObject = exampleObject;
        }
    }
}
