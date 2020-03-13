package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OpenApiResponse.class, scope = PROTOTYPE)
public class OpenApiResponse implements Implementor {

    @Property("Example")
    public String example;
    @Property("Schema")
    public String schema;
    @Property("Media Type")
    public String mediaType;

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
