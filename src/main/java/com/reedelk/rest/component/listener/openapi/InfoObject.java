package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.openapi.Serializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = InfoObject.class, scope = PROTOTYPE)
public class InfoObject implements Implementor, Serializable {

    @Property("Title")
    @Hint("My API")
    @InitValue("My API")
    @DefaultValue("My API")
    @Description("The title of the API.")
    private String title;

    @Property("Description")
    @Hint("My API description")
    @Description("A short description of the API.")
    private String description;

    @Property("Terms URL")
    @Hint("http://example.domain.com/terms.html")
    @Description("A URL to the Terms of Service for the API. MUST be in the format of a URL.")
    private String termsOfService;

    @Property("Version")
    @Hint("v1")
    @Example("v1")
    @InitValue("v1")
    @DefaultValue("v1")
    @Description("The version of the OpenAPI document.")
    private String version;

    @Property("Contact")
    private ContactObject contact;

    @Property("License")
    private LicenseObject license;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public ContactObject getContact() {
        return contact;
    }

    public void setContact(ContactObject contact) {
        this.contact = contact;
    }

    public LicenseObject getLicense() {
        return license;
    }

    public void setLicense(LicenseObject license) {
        this.license = license;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public JSONObject serialize() {
        return null;
    }
}
