package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;

@Component(service = ServerVariableObject.class, scope = ServiceScope.PROTOTYPE)
public class ServerVariableObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.model.ServerVariableObject> {

    @Property("Description")
    @Hint("Hosts variable")
    @Example("Hosts variable")
    @Description("An optional description for the server variable.")
    private String description;

    @Property("Default")
    @DefaultValue("default")
    @Hint("development")
    @Example("development")
    @Description("The default value to use for substitution, and to send, if an alternate value is not supplied. " +
            "Unlike the Schema Object's default, this value MUST be provided by the consumer.")
    private String defaultValue;

    @TabGroup("Variable Enum Options")
    @Property("Variable Enum Options")
    @Description("An enumeration of string values to be used if the substitution options are from a limited set.")
    private List<String> enumValues;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    @Override
    public com.reedelk.runtime.openapi.v3.model.ServerVariableObject map(OpenApiSerializableContext context) {
        com.reedelk.runtime.openapi.v3.model.ServerVariableObject mappedServerVariable =
                new com.reedelk.runtime.openapi.v3.model.ServerVariableObject();
        mappedServerVariable.setDescription(description);
        mappedServerVariable.setDefaultValue(defaultValue);
        mappedServerVariable.setEnumValues(enumValues);
        return mappedServerVariable;
    }
}
