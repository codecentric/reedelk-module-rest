package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OpenApiServerDefinition.class, scope = PROTOTYPE)
public class OpenApiServerDefinition implements Implementor {

    @Property("Description")
    private String description;

    @Property("URL Variables")
    @TabGroup("URL Variables")
    @KeyName("Variable Name")
    @ValueName("Variable Definition")
    private Map<String, OpenApiServerVariableDefinition> variables;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, OpenApiServerVariableDefinition> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, OpenApiServerVariableDefinition> variables) {
        this.variables = variables;
    }
}
