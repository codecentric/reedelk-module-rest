package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ComponentsObject.class, scope = PROTOTYPE)
public class ComponentsObject implements Implementor, OpenApiSerializable {

    @Property("Schemas")
    @TabGroup("Schemas")
    private Map<String, SchemaObject> schemas;

    public Map<String, SchemaObject> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, SchemaObject> schemas) {
        this.schemas = schemas;
    }

    @Override
    public JSONObject serialize() {
        JSONObject allSchemas = JsonObjectFactory.newJSONObject();
        if (!schemas.isEmpty()) {
            schemas.forEach((schemaName, schemaObject) ->
                    allSchemas.put(schemaName, new JSONObject(schemaObject.getSchema())));
        }
        JSONObject components = JsonObjectFactory.newJSONObject();
        components.put("schemas", allSchemas);
        return components;
    }
}
