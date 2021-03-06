package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.openapi.v3.model.Schema;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.commons.StreamUtils;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.HashMap;
import java.util.Map;

@Component(service = MediaTypeObject.class, scope = ServiceScope.PROTOTYPE)
public class MediaTypeObject implements Implementor, OpenAPIModel<de.codecentric.reedelk.openapi.v3.model.MediaTypeObject> {

    @Property("Inline Schema")
    @DefaultValue("false")
    @Example("true")
    @Description("If true, the schema is in-lined in the final OpenAPI document instead " +
            "of referencing the schema from the Components object.")
    private Boolean inlineSchema;

    @Property("Schema")
    @WidthAuto
    @Hint("assets/data_model.json")
    @HintBrowseFile("Select Schema File ...")
    @Example("assets/data_model.json")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText schema;

    @Property("Example")
    @WidthAuto
    @Hint("assets/data_model.json")
    @Example("assets/data_model.json")
    @HintBrowseFile("Select Example File ...")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText example;

    @Property("Examples")
    @TabGroup("Examples")
    @Hint("assets/data_model.json")
    @Description("The path and name of the files to be read from the project's resources folder.")
    private Map<String, ExampleObject> examples;

    public ResourceText getSchema() {
        return schema;
    }

    public void setSchema(ResourceText schema) {
        this.schema = schema;
    }

    public Boolean getInlineSchema() {
        return inlineSchema;
    }

    public void setInlineSchema(Boolean inlineSchema) {
        this.inlineSchema = inlineSchema;
    }

    public ResourceText getExample() {
        return example;
    }

    public void setExample(ResourceText example) {
        this.example = example;
    }

    public Map<String, ExampleObject> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, ExampleObject> examples) {
        this.examples = examples;
    }

    @Override
    public de.codecentric.reedelk.openapi.v3.model.MediaTypeObject map(OpenApiSerializableContext context) {
        de.codecentric.reedelk.openapi.v3.model.MediaTypeObject mappedMediaType =
                new de.codecentric.reedelk.openapi.v3.model.MediaTypeObject();

        // Schema
        if (schema != null) {
            Schema mappedSchema = context.getSchema(schema, inlineSchema);
            mappedMediaType.setSchema(mappedSchema);
        }

        // Example
        if (example != null) {
            String exampleData = StreamUtils.FromString.consume(example.data());
            mappedMediaType.setExample(new de.codecentric.reedelk.openapi.v3.model.Example(exampleData));
        }

        // Examples
        if (examples != null) {
            Map<String, de.codecentric.reedelk.openapi.v3.model.ExampleObject> mappedExamples = new HashMap<>();
            this.examples.forEach((exampleId, exampleObject) -> {
                de.codecentric.reedelk.openapi.v3.model.ExampleObject mappedExampleObject = exampleObject.map(context);
                mappedExamples.put(exampleId, mappedExampleObject);
            });
            mappedMediaType.setExamples(mappedExamples);
        }

        return mappedMediaType;
    }
}
