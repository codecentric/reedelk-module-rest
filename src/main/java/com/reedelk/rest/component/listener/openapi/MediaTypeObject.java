package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = MediaTypeObject.class, scope = PROTOTYPE)
public class MediaTypeObject implements Implementor, OpenApiSerializable {

    @Property("Description")
    private String description;

    @Property("Example")
    @Hint("assets/data_model.json")
    @Example("assets/data_model.json")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText example;

    @Property("Schema")
    @Hint("assets/data_model.json")
    @Example("assets/data_model.json")
    @Description("The path and name of the file to be read from the project's resources folder.")
    private ResourceText schema;

    @Property("Media Type")
    @MimeTypeCombo
    @InitValue(MimeType.MIME_TYPE_APPLICATION_JSON)
    public String mediaType;

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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JSONObject serialize() {
        JSONObject mediaType = JsonObjectFactory.newJSONObject();
        if (description != null) mediaType.put("description", description);
        if (schema != null) mediaType.put("schema", schema.data());
        if (example != null) mediaType.put("example", example);
        return mediaType;
    }
}
