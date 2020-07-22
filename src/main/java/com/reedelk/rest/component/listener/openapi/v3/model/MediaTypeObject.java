package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.v3.model.ExampleReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = MediaTypeObject.class, scope = ServiceScope.PROTOTYPE)
public class MediaTypeObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.model.MediaTypeObject> {

    @Property("Example")
    @WidthAuto
    @Hint("assets/data_model.json")
    @Example("assets/data_model.json")
    @HintBrowseFile("Select Example File ...")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText example;

    @Property("Schema")
    @WidthAuto
    @Hint("assets/data_model.json")
    @HintBrowseFile("Select Schema File ...")
    @Example("assets/data_model.json")
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
    public com.reedelk.runtime.openapi.v3.model.MediaTypeObject map(OpenApiSerializableContext context) {
        com.reedelk.runtime.openapi.v3.model.MediaTypeObject mappedMediaType =
                new com.reedelk.runtime.openapi.v3.model.MediaTypeObject();
        // Schema
        if (schema != null) mappedMediaType.setSchema(SchemaUtils.toSchemaReference(schema, context));

        // Example
        if (example != null) {
            String exampleData = StreamUtils.FromString.consume(example.data());
            mappedMediaType.setExample(new ExampleReference(exampleData));
        }
        return mappedMediaType;
    }
}
