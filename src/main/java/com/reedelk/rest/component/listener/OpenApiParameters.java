package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiParameters.class, scope = PROTOTYPE)
public class OpenApiParameters implements Implementor {

    @Property("Parameters")
    @KeyName("Parameter Name")
    @ValueName("Parameter Definition")
    private Map<String, OpenApiParameterDefinition> parameters;

    public Map<String, OpenApiParameterDefinition> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, OpenApiParameterDefinition> parameters) {
        this.parameters = parameters;
    }
}