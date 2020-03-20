package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = OpenApiHeaderDefinition.class, scope = PROTOTYPE)
public class OpenApiHeaderDefinition implements Implementor {

    @Property("Description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
