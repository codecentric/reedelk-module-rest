package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ComponentsObject.class, scope = PROTOTYPE)
public class ComponentsObject implements Implementor, OpenApiSerializable {

    @Property("Schemas")
    @KeyName("Schema Name")
    @ValueName("Schema Definition")
    @TabGroup("Schemas")
    private Map<String, SchemaObject> schemas;

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    // TODO: If the schema is in the context, then we just add it to the context.
    @Override
    public JSONObject serialize() {
        JSONObject serialized = JsonObjectFactory.newJSONObject();

        set(serialized, "schemas", schemas);
        return new JSONObject(schemas);
    }
}
