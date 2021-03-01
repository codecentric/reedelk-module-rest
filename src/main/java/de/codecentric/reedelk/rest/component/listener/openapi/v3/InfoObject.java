package de.codecentric.reedelk.rest.component.listener.openapi.v3;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import java.util.Optional;

@Collapsible
@Component(service = InfoObject.class, scope = ServiceScope.PROTOTYPE)
public class InfoObject implements Implementor, OpenAPIModel<de.codecentric.reedelk.openapi.v3.model.InfoObject> {

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
    @Description("The version of the API.")
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
    public de.codecentric.reedelk.openapi.v3.model.InfoObject map(OpenApiSerializableContext context) {
        de.codecentric.reedelk.openapi.v3.model.InfoObject mappedInfo =
                new de.codecentric.reedelk.openapi.v3.model.InfoObject();
        mappedInfo.setTitle(Optional.ofNullable(title).orElse("My API"));
        mappedInfo.setDescription(description);
        mappedInfo.setTermsOfService(termsOfService);
        mappedInfo.setVersion(Optional.ofNullable(version).orElse("1.0.0"));
        if (contact != null) mappedInfo.setContact(contact.map(context));
        if (license != null) mappedInfo.setLicense(license.map(context));
        return mappedInfo;
    }
}
