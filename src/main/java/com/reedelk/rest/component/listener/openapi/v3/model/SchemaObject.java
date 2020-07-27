package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import com.reedelk.runtime.openapi.v3.model.SchemaReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = SchemaObject.class, scope = ServiceScope.PROTOTYPE)
public class SchemaObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.model.SchemaObject> {

    @Property("Schema")
    @WidthAuto
    @Hint("assets/data_model.json")
    @Example("assets/data_model.json")
    @HintBrowseFile("Select Schema File ...")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText schema;

    public ResourceText getSchema() {
        return schema;
    }

    public void setSchema(ResourceText schema) {
        this.schema = schema;
    }

    @Override
    public com.reedelk.runtime.openapi.v3.model.SchemaObject map(OpenApiSerializableContext context) {
        com.reedelk.runtime.openapi.v3.model.SchemaObject mappedSchema =
                new com.reedelk.runtime.openapi.v3.model.SchemaObject();
        SchemaReference schemaReference = SchemaUtils.toSchemaReference(schema);
        mappedSchema.setSchema(schemaReference, context);
        return mappedSchema;
    }
}
