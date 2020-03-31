package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = MediaTypeObject.class, scope = PROTOTYPE)
public class MediaTypeObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Example")
    @Hint("assets/data_model.json")
    @Example("assets/data_model.json")
    @HintBrowseFile("Select Example File ...")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText example;

    @Property("Schema")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        String schemaReference = context.schemaReferenceOf(schema);
        JSONObject schemaReferenceObject = JsonObjectFactory.newJSONObject();
        schemaReferenceObject.put("$ref", schemaReference);
        set(serialized, "schema", schemaReferenceObject);
        set(serialized, "example", example);
        return serialized;
    }
}
