package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.List;

@Component(service = SecurityRequirementObject.class, scope = ServiceScope.PROTOTYPE)
public class SecurityRequirementObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.model.SecurityRequirementObject> {

    @Property("Security Requirement Name")
    @Description("Each name MUST correspond to a security scheme which is declared in the Security Schemes under the Components Object. If the security scheme is of type \"oauth2\" or \"openIdConnect\", then the value is a list of scope names required for the execution, and the list MAY be empty if authorization does not require a specified scope. For other security scheme types, the array MUST be empty.")
    private String name;

    @Property("Scope Names")
    @TabGroup("scopes")
    private List<String> scopes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public com.reedelk.openapi.v3.model.SecurityRequirementObject map(OpenApiSerializableContext context) {
        return null;
    }
}
