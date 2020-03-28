package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiComponentsDefinition.class, scope = PROTOTYPE)
public class OpenApiComponentsDefinition implements Implementor {

    @Property("Schemas")
    private Map<String, OpenApiSchemaDefinition> schemas;

    public Map<String, OpenApiSchemaDefinition> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, OpenApiSchemaDefinition> schemas) {
        this.schemas = schemas;
    }
}
