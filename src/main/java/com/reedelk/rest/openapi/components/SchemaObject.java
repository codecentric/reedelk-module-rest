package com.reedelk.rest.openapi.components;

public class SchemaObject {

    private String name;
    private String schema;
    private String schemaResourcePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchemaResourcePath() {
        return schemaResourcePath;
    }

    public void setSchemaResourcePath(String schemaResourcePath) {
        this.schemaResourcePath = schemaResourcePath;
    }

    public String getRef() {
        return null;
    }
}
