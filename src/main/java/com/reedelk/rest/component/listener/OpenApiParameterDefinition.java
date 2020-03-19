package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OpenApiParameterDefinition.class, scope = PROTOTYPE)
public class OpenApiParameterDefinition implements Implementor {

    @Property("Description")
    private String description;

    @Property("In")
    private ParameterLocation in;

    @Property("Required")
    private Boolean required;

    @Property("Deprecated")
    private Boolean deprecated;

    public ParameterLocation getIn() {
        return in;
    }

    public void setIn(ParameterLocation in) {
        this.in = in;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }
}
