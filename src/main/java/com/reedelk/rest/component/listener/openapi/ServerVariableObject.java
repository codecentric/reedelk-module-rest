package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.Hint;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.List;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = ServerVariableObject.class, scope = PROTOTYPE)
public class ServerVariableObject implements Implementor, OpenApiSerializable {

    @Property("Description")
    private String description;

    @Property("Default")
    private String defaultValue;

    @Property("Enum Values")
    @Hint("Variable enum value")
    @TabGroup("Enum Values")
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
    public JSONObject serialize() {
        return null;
    }
}
