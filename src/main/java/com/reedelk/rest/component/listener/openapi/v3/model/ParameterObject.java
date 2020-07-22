package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.openapi.v3.OpenApiSerializableContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = ParameterObject.class, scope = ServiceScope.PROTOTYPE)
public class ParameterObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.model.ParameterObject> {

    @Property("Name")
    @Hint("myParam")
    @Example("myParam")
    @Description("The name of the parameter. Parameter names are case sensitive. " +
            "If in is 'Path', the name field MUST correspond to a template expression occurring within the path field in the Paths Object. " +
            "For all other cases, the name corresponds to the parameter name used by the in property.")
    private String name;

    @Property("Description")
    @Hint("My parameter description")
    @Example("My parameter description")
    @Description("A brief description of the parameter. This could contain examples of use.")
    private String description;

    @Property("In")
    @Example("header")
    @InitValue("query")
    @DefaultValue("query")
    @Description("The location of the parameter. Possible values are 'query', 'header', 'path' or 'cookie'.")
    private ParameterLocation in;

    @Property("Style")
    @Example("form")
    @InitValue("form")
    @DefaultValue("form")
    @Description("Describes how the parameter value will be serialized depending on the type of the parameter value. " +
            "Default values (based on value of in): for query - form; for path - simple; for header - simple; for cookie - form.")
    private ParameterStyle style = ParameterStyle.form;

    @Property("Schema")
    @InitValue("STRING")
    @DefaultValue("STRING")
    private PredefinedSchema predefinedSchema = PredefinedSchema.STRING;

    @Property("Custom Schema")
    @WidthAuto
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParameterLocation getIn() {
        return in;
    }

    public void setIn(ParameterLocation in) {
        this.in = in;
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
    public com.reedelk.runtime.openapi.v3.model.ParameterObject map(OpenApiSerializableContext context) {
        com.reedelk.runtime.openapi.v3.model.ParameterObject mappedParameter =
                new com.reedelk.runtime.openapi.v3.model.ParameterObject();
        mappedParameter.setName(name);
        mappedParameter.setDescription(description);
        mappedParameter.setIn(com.reedelk.runtime.openapi.v3.model.ParameterLocation.valueOf(in.name()));
        mappedParameter.setStyle(com.reedelk.runtime.openapi.v3.model.ParameterStyle.valueOf(style.name()));
        mappedParameter.setPredefinedSchema(com.reedelk.runtime.openapi.v3.PredefinedSchema.valueOf(predefinedSchema.name()));
        if (schema != null) mappedParameter.setSchema(SchemaUtils.toSchemaReference(schema, context));
        mappedParameter.setExample(example);
        mappedParameter.setExplode(explode);
        mappedParameter.setDeprecated(deprecated);
        mappedParameter.setRequired(required);
        mappedParameter.setAllowEmptyValue(allowEmptyValue);
        mappedParameter.setAllowReserved(allowReserved);
        return mappedParameter;
    }
}
