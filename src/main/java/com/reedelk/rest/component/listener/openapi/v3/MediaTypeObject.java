package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableContext;
import com.reedelk.openapi.v3.Example;
import com.reedelk.openapi.v3.Schema;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = MediaTypeObject.class, scope = ServiceScope.PROTOTYPE)
public class MediaTypeObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.MediaTypeObject> {

    @Property("Example")
    @WidthAuto
    @Hint("assets/data_model.json")
    @com.reedelk.runtime.api.annotation.Example("assets/data_model.json")
    @HintBrowseFile("Select Example File ...")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText example;

    @Property("Schema")
    @WidthAuto
    @Hint("assets/data_model.json")
    @HintBrowseFile("Select Schema File ...")
    @com.reedelk.runtime.api.annotation.Example("assets/data_model.json")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText schema;

    public ResourceText getExample() {
        return example;
    }

    public void setExample(ResourceText example) {
        this.example = example;
    }

    public ResourceText getSchema() {
        return schema;
    }

    public void setSchema(ResourceText schema) {
        this.schema = schema;
    }

    @Override
    public com.reedelk.openapi.v3.MediaTypeObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.MediaTypeObject mappedMediaType =
                new com.reedelk.openapi.v3.MediaTypeObject();
        // Schema
        if (schema != null) {
            Schema schema = SchemaUtils.toSchemaReference(this.schema);
            mappedMediaType.setSchema(schema, context);
        }

        // Example
        if (example != null) {
            String exampleData = StreamUtils.FromString.consume(example.data());
            mappedMediaType.setExample(new Example(exampleData));
        }
        return mappedMediaType;
    }
}
