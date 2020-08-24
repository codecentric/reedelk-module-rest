package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.v3.model.Schema;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;

@Component(service = MediaTypeObject.class, scope = ServiceScope.PROTOTYPE)
public class MediaTypeObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.model.MediaTypeObject> {

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
    @HintBrowseFile("Select Example File ...")
    @ListInputType(ListInputType.ListInput.FILE)
    @Description("The path and name of the files to be read from the project's resources folder.")
    private List<ResourceText> examples;

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

    public List<ResourceText> getExamples() {
        return examples;
    }

    public void setExamples(List<ResourceText> examples) {
        this.examples = examples;
    }

    @Override
    public com.reedelk.openapi.v3.model.MediaTypeObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.model.MediaTypeObject mappedMediaType =
                new com.reedelk.openapi.v3.model.MediaTypeObject();

        // TODO: Map in the model multiple examples

        // Schema
        if (schema != null) {
            Schema mappedSchema = context.getSchema(schema, inlineSchema);
            mappedMediaType.setSchema(mappedSchema);
        }

        // Example
        if (example != null) {
            String exampleData = StreamUtils.FromString.consume(example.data());
            mappedMediaType.setExample(new com.reedelk.openapi.v3.model.Example(exampleData));
        }
        return mappedMediaType;
    }
}
