package com.reedelk.rest.component.listener.openapi;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = LicenseObject.class, scope = PROTOTYPE)
public class LicenseObject implements Implementor {

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
}
