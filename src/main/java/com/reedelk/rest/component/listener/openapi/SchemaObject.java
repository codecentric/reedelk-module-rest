package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = SchemaObject.class, scope = PROTOTYPE)
public class SchemaObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Schema")
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
    public JSONObject serialize(OpenApiSerializableContext context) {
        String jsonSchema = StreamUtils.FromString.consume(schema.data());
        JSONObject schemaAsJsonObject = new JSONObject(jsonSchema);
        if (schemaAsJsonObject.has("name")) {
            // we must remove the name property if present.
            schemaAsJsonObject.remove("name");
        }
        return schemaAsJsonObject;
    }
}
