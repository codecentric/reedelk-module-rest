package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiInfoDefinition.class, scope = PROTOTYPE)
public class OpenApiInfoDefinition implements Implementor {

    @Property("Title") // TODO: Required
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
    private OpenApiContactDefinition contact;

    @Property("License")
    private OpenApiLicenseDefinition license;

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

    public OpenApiContactDefinition getContact() {
        return contact;
    }

    public void setContact(OpenApiContactDefinition contact) {
        this.contact = contact;
    }

    public OpenApiLicenseDefinition getLicense() {
        return license;
    }

    public void setLicense(OpenApiLicenseDefinition license) {
        this.license = license;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
