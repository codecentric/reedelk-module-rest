package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;


@Component(service = SecuritySchemeObject.class, scope = ServiceScope.PROTOTYPE)
public class SecuritySchemeObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.model.SecuritySchemeObject> {

    @Property("Type")
    @InitValue("apiKey")
    @Hint("oauth2")
    @Example("apiKey")
    @Description("The type of the security scheme. Valid values are \"apiKey\", \"http\", \"oauth2\", \"openIdConnect\".")
    private SecurityType type;

    @Property("Description")
    @Hint("My security scheme description")
    @Example("My security scheme description")
    @Description("A short description for security scheme.")
    private String description;

    @Property("Name")
    @Hint("api_key")
    @Example("api_key")
    @Description("The name of the header, query or cookie parameter to be used.")
    @When(propertyName = "type", propertyValue = "apiKey")
    private String name;

    @Property("In")
    @Hint("header")
    @InitValue("header")
    @Example("header")
    @Description("The location of the API key. Valid values are \"query\", \"header\" or \"cookie\".")
    @When(propertyName = "type", propertyValue = "apiKey")
    private SecurityKeyLocation in;

    @Property("Scheme")
    @Hint("basic")
    @Example("basic")
    @Description("The name of the HTTP Authorization scheme to be used in the Authorization header as defined in RFC7235. " +
            "The values used SHOULD be registered in the IANA Authentication Scheme registry.")
    @When(propertyName = "type", propertyValue = "http")
    private String scheme;

    @Property("Bearer Format")
    @Hint("JWT")
    @Example("JWT")
    @Description("A hint to the client to identify how the bearer token is formatted. Bearer tokens are usually generated " +
            "by an authorization server, so this information is primarily for documentation purposes.")
    @When(propertyName = "type", propertyValue = "http")
    private String bearerFormat;

    @Property("Flows")
    @Description("An object containing configuration information for the flow types supported.")
    @When(propertyName = "type", propertyValue = "oauth2")
    private OAuthFlowsObject flows;

    @Property("OpenID Connect URL")
    @Description("OpenId Connect URL to discover OAuth2 configuration values. This MUST be in the form of a URL.")
    @When(propertyName = "type", propertyValue = "openIdConnect")
    private String openIdConnectUrl;

    public SecurityType getType() {
        return type;
    }

    public void setType(SecurityType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SecurityKeyLocation getIn() {
        return in;
    }

    public void setIn(SecurityKeyLocation in) {
        this.in = in;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getBearerFormat() {
        return bearerFormat;
    }

    public void setBearerFormat(String bearerFormat) {
        this.bearerFormat = bearerFormat;
    }

    public OAuthFlowsObject getFlows() {
        return flows;
    }

    public void setFlows(OAuthFlowsObject flows) {
        this.flows = flows;
    }

    public String getOpenIdConnectUrl() {
        return openIdConnectUrl;
    }

    public void setOpenIdConnectUrl(String openIdConnectUrl) {
        this.openIdConnectUrl = openIdConnectUrl;
    }

    @Override
    public com.reedelk.openapi.v3.model.SecuritySchemeObject map(OpenApiSerializableContext context) {
        // TODO: Fixme
        return null;
    }
}
