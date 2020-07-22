package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.openapi.v3.model.SchemaReference;

public class ModuleSchemaReference implements SchemaReference {

    private final String schemaId;
    private final String schemaData;

    public ModuleSchemaReference(String schemaId, String schemaData) {
        this.schemaId = schemaId;
        this.schemaData = schemaData;
    }

    @Override
    public String getSchemaId() {
        return schemaId;
    }

    @Override
    public String getSchemaData() {
        return schemaData;
    }
}
