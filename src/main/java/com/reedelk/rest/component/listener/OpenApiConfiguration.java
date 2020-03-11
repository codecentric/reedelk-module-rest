package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Collapsible;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = OpenApiConfiguration.class, scope = PROTOTYPE)
public class OpenApiConfiguration implements Implementor {

    @Property("Exclude")
    private Boolean exclude;

    @Property("Responses")
    private Map<String, OpenApiResponse> responses;

    public Boolean getExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

    public Map<String, OpenApiResponse> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, OpenApiResponse> responses) {
        this.responses = responses;
    }
}
