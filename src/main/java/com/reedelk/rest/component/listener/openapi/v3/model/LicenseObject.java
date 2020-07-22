package com.reedelk.rest.component.listener.openapi.v3.model;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Collapsible
@Component(service = LicenseObject.class, scope = ServiceScope.PROTOTYPE)
public class LicenseObject implements Implementor, OpenAPIModel<com.reedelk.runtime.openapi.v3.model.LicenseObject> {

    @Property("Name")
    @Hint("Apache 2.0")
    @Example("Apache 2.0")
    @Description("The license name used for the API.")
    private String name;

    @Property("URL")
    @Hint("http://www.apache.org/licenses/LICENSE-2.0.html")
    @Example("http://www.apache.org/licenses/LICENSE-2.0.html")
    @Description("A URL to the license used for the API. MUST be in the format of a URL.")
    private String url;

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

    @Override
    public com.reedelk.runtime.openapi.v3.model.LicenseObject map() {
        com.reedelk.runtime.openapi.v3.model.LicenseObject mappedLicense =
                new com.reedelk.runtime.openapi.v3.model.LicenseObject();
        mappedLicense.setName(name);
        mappedLicense.setUrl(url);
        return mappedLicense;
    }
}
