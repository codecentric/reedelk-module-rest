package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;


@Component(service = OAuthFlowsObject.class, scope = ServiceScope.PROTOTYPE)
public class OAuthFlowsObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.model.OAuthFlowsObject> {

    @Property("Implicit")
    @Description("Configuration for the OAuth Implicit flow.")
    private OAuthFlowObject implicit;

    @Property("Password")
    @Description("Configuration for the OAuth Resource Owner Password flow.")
    private OAuthFlowObject password;

    @Property("Client Credentials")
    @Description("Configuration for the OAuth Client Credentials flow. Previously called application in OpenAPI 2.0.")
    private OAuthFlowObject clientCredentials;

    @Property("Authorization Code")
    @Description("Configuration for the OAuth Authorization Code flow. Previously called accessCode in OpenAPI 2.0.")
    private OAuthFlowObject authorizationCode;

    public OAuthFlowObject getImplicit() {
        return implicit;
    }

    public void setImplicit(OAuthFlowObject implicit) {
        this.implicit = implicit;
    }

    public OAuthFlowObject getPassword() {
        return password;
    }

    public void setPassword(OAuthFlowObject password) {
        this.password = password;
    }

    public OAuthFlowObject getClientCredentials() {
        return clientCredentials;
    }

    public void setClientCredentials(OAuthFlowObject clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public OAuthFlowObject getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(OAuthFlowObject authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    @Override
    public com.reedelk.openapi.v3.model.OAuthFlowsObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.model.OAuthFlowsObject mapped = new com.reedelk.openapi.v3.model.OAuthFlowsObject();
        if (implicit != null) mapped.setImplicit(implicit.map(context));
        if (password != null) mapped.setPassword(password.map(context));
        if (clientCredentials != null) mapped.setClientCredentials(clientCredentials.map(context));
        if (authorizationCode != null) mapped.setAuthorizationCode(authorizationCode.map(context));
        return mapped;
    }
}
