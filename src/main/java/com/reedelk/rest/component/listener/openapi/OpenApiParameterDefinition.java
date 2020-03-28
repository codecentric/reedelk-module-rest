package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.component.listener.ParameterLocation;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OpenApiParameterDefinition.class, scope = PROTOTYPE)
public class OpenApiParameterDefinition implements Implementor {

    @Property("Description")
    @Hint("My parameter description")
    @Description("A brief description of the parameter. This could contain examples of use.")
    private String description;

    @Property("In")
    @DefaultValue("QUERY")
    @Description("The location of the parameter. Possible values are 'query', 'header', 'path' or 'cookie'.")
    private ParameterLocation in;

    @Property("Required")
    @DefaultValue("false")
    @Description("Determines whether this parameter is mandatory. Otherwise, the property MAY be included and its default value is false.")
    @When(propertyName = "in", propertyValue = "PATH")
    private Boolean required;

    @Property("Deprecated")
    @Description("Specifies that a parameter is deprecated and SHOULD be transitioned out of usage.")
    private Boolean deprecated;

    @Property("Allow Empty")
    @When(propertyName = "in", propertyValue = "QUERY")
    @Description("Sets the ability to pass empty-valued parameters. This is valid only for query parameters and allows sending a parameter with an empty value.")
    private Boolean allowEmptyValue;

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

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }
}
