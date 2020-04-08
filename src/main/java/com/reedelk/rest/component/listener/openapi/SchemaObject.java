package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.internal.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.internal.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.Arrays;
import java.util.List;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = SchemaObject.class, scope = PROTOTYPE)
public class SchemaObject extends AbstractOpenApiSerializable implements Implementor {

    private static final List<String> PROPERTIES_TO_EXCLUDE_FROM_SCHEMA = Arrays.asList("$id", "$schema", "name");

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
    public JSONObject serialize(OpenApiSerializableContext context) {
        String jsonSchema = StreamUtils.FromString.consume(schema.data());
        JSONObject schemaAsJsonObject = new JSONObject(jsonSchema);
        PROPERTIES_TO_EXCLUDE_FROM_SCHEMA.forEach(propertyName ->
                removePropertyIfExists(schemaAsJsonObject, propertyName));
        return schemaAsJsonObject;
    }

    private void removePropertyIfExists(JSONObject jsonObject, String propertyName) {
        if (jsonObject.has(propertyName)) {
            jsonObject.remove(propertyName);
        }
    }
}
