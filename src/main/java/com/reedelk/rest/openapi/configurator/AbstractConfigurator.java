package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.components.Components;
import com.reedelk.rest.openapi.components.SchemaObject;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.resource.ResourceText;

abstract class AbstractConfigurator implements Configurator {

    protected String getSchemaRefFrom(OpenAPI api, ResourceText resourceText) {
        // If schema exists
        String jsonSchema = StreamUtils.FromString.consume(resourceText.data());
        String schemaPath = resourceText.path();

        Components components = api.getComponents();
        SchemaObject schemaObject = new SchemaObject();

        //components.add("", );
        // TODO: Schema
        return null;
    }
}
