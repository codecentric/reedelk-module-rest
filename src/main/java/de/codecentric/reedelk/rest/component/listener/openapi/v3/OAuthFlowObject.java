package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.Map;

@Collapsible
@Component(service = OAuthFlowObject.class, scope = ServiceScope.PROTOTYPE)
public class OAuthFlowObject implements Implementor, OpenAPIModel<de.codecentric.reedelk.openapi.v3.model.OAuthFlowObject> {

    @Property("Authorization URL")
    @Hint("https://example.com/api/oauth/dialog")
    @Example("https://example.com/api/oauth/dialog")
    @Description("The authorization URL to be used for this flow. This MUST be in the form of a URL.")
    private String authorizationUrl;

    @Property("Token URL")
    @Hint("https://example.com/api/oauth/token")
    @Example("https://example.com/api/oauth/token")
    @Description("The token URL to be used for this flow. This MUST be in the form of a URL.")
    private String tokenUrl;

    @Property("Refresh URL")
    @Hint("https://example.com/api/oauth/refresh")
    @Example("https://example.com/api/oauth/refresh")
    @Description("The URL to be used for obtaining refresh tokens. This MUST be in the form of a URL.")
    private String refreshUrl;

    @Property("Scopes")
    @Example("\"read:pets\": \"read your pets\"")
    @Description("The available scopes for the OAuth2 security scheme. " +
            "A map between the scope name and a short description for it. The map MAY be empty.")
    @TabGroup("scopes")
    private Map<String,String> scopes;

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    public Map<String, String> getScopes() {
        return scopes;
    }

    public void setScopes(Map<String, String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public de.codecentric.reedelk.openapi.v3.model.OAuthFlowObject map(OpenApiSerializableContext context) {
        de.codecentric.reedelk.openapi.v3.model.OAuthFlowObject mapped = new de.codecentric.reedelk.openapi.v3.model.OAuthFlowObject();
        mapped.setAuthorizationUrl(authorizationUrl);
        mapped.setRefreshUrl(refreshUrl);
        mapped.setTokenUrl(tokenUrl);
        mapped.setScopes(scopes);
        return mapped;
    }
}
