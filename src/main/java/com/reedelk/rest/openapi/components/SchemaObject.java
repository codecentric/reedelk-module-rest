package com.reedelk.rest.openapi.components;

public class SchemaObject {

    private String name;
    private String schema;
    private String schemaResourcePath;

    public SchemaObject(String name, String schemaResourcePath, String schema) {
        this.name = name;
        this.schema = schema;
        this.schemaResourcePath = schemaResourcePath;
    }

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
