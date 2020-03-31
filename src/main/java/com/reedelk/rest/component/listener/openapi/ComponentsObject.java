package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.AbstractOpenApiSerializable;
import com.reedelk.rest.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ComponentsObject.class, scope = PROTOTYPE)
public class ComponentsObject extends AbstractOpenApiSerializable implements Implementor {

    @Property("Schemas")
    @KeyName("Schema Name")
    @ValueName("Schema Definition")
    @TabGroup("Schemas")
    private Map<String, SchemaObject> schemas = new HashMap<>();

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    @Override
    public JSONObject serialize(OpenApiSerializableContext context) {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "schemas", schemas, context);
        return serialized;
    }
}
