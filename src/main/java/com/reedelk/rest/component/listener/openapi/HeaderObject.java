package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = HeaderObject.class, scope = PROTOTYPE)
public class HeaderObject implements Implementor, OpenApiSerializable {

    @Property("Description")
    @Hint("My header description")
    @Example("My header description")
    @Description("A brief description of the header. This could contain examples of use.")
    private String description;

    @Property("Style")
    @Example("form")
    @InitValue("simple")
    @DefaultValue("simple")
    @Description("Describes how the parameter value will be serialized depending on the type of the parameter value. " +
            "Default values (based on value of in): for query - form; for path - simple; for header - simple; for cookie - form.")
    private ParameterStyle style;

    @Property("Schema")
    @InitValue("STRING")
    @DefaultValue("STRING")
    private PredefinedSchema predefinedSchema;

    @Property("Custom Schema")
    @HintBrowseFile("Select Schema File ...")
    @When(propertyName = "predefinedSchema", propertyValue = "NONE")
    @When(propertyName = "predefinedSchema", propertyValue = When.NULL)
    private ResourceText schema;

    @Property("Example")
    @Hint("myParamValue")
    @Example("myParamValue")
    @Description("Example of the parameter's potential value. " +
            "The example SHOULD match the specified schema and encoding properties if present. ")
    private String example;

    @Property("Explode")
    @DefaultValue("false")
    @Description("When this is true, parameter values of type array or object generate separate parameters " +
            "for each value of the array or key-value pair of the map. For other types of parameters this property " +
            "has no effect. When style is form, the default value is true. " +
            "For all other styles, the default value is false.")
    private Boolean explode;

    @Property("Deprecated")
    @Description("Specifies that a parameter is deprecated and SHOULD be transitioned out of usage.")
    private Boolean deprecated;

    @Property("Required")
    @DefaultValue("false")
    @Description("Determines whether this parameter is mandatory. " +
            "Otherwise, the property MAY be included and its default value is false.")
    @When(propertyName = "in", propertyValue = "PATH")
    private Boolean required;

    @Property("Allow Empty")
    @DefaultValue("false")
    @When(propertyName = "in", propertyValue = "QUERY")
    @Description("Sets the ability to pass empty-valued parameters. " +
            "This is valid only for query parameters and allows sending a parameter with an empty value.")
    private Boolean allowEmptyValue;

    @Property("Allow Reserved")
    @Description("Determines whether the parameter value SHOULD allow reserved characters, " +
            "as defined by RFC3986 :/?#[]@!$&'()*+,;= to be included without percent-encoding. " +
            "This property only applies to parameters with an in value of query.")
    private Boolean allowReserved;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParameterStyle getStyle() {
        return style;
    }

    public void setStyle(ParameterStyle style) {
        this.style = style;
    }

    public PredefinedSchema getPredefinedSchema() {
        return predefinedSchema;
    }

    public void setPredefinedSchema(PredefinedSchema predefinedSchema) {
        this.predefinedSchema = predefinedSchema;
    }

    public ResourceText getSchema() {
        return schema;
    }

    public void setSchema(ResourceText schema) {
        this.schema = schema;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Boolean getExplode() {
        return explode;
    }

    public void setExplode(Boolean explode) {
        this.explode = explode;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public Boolean getAllowReserved() {
        return allowReserved;
    }

    public void setAllowReserved(Boolean allowReserved) {
        this.allowReserved = allowReserved;
    }

    @Override
    public JSONObject serialize() {
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "description", description);
        return serialized;
    }
}
