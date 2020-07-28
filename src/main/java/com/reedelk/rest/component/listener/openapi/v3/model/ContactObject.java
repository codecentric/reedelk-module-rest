package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.openapi.OpenApiSerializableContext;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Collapsible
@Component(service = ContactObject.class, scope = ServiceScope.PROTOTYPE)
public class ContactObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.ContactObject> {

    @Property("Name")
    @Hint("API Support")
    @Example("API Support")
    @Description("The identifying name of the contact person/organization.")
    private String name;

    @Property("URL")
    @Hint("http://www.example.com/support")
    @Example("http://www.example.com/support")
    @Description("The URL pointing to the contact information. MUST be in the format of a URL.")
    private String url;

    @Property("Email")
    @Hint("support@example.com")
    @Example("support@example.com")
    @Description("The email address of the contact person/organization. MUST be in the format of an email address.")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public com.reedelk.openapi.v3.ContactObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.ContactObject mappedContact =
                new com.reedelk.openapi.v3.ContactObject();
        mappedContact.setEmail(email);
        mappedContact.setName(name);
        mappedContact.setUrl(url);
        return mappedContact;
    }
}
