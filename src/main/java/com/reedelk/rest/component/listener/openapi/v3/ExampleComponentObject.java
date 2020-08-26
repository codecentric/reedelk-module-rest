package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.commons.StreamUtils;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = ExampleComponentObject.class, scope = ServiceScope.PROTOTYPE)
public class ExampleComponentObject implements Implementor, OpenAPIModel<com.reedelk.openapi.v3.model.ExampleObject> {

    // The example within a component object cannot be inlined,
    // it is by default inlined.

    @Property("Summary")
    @Hint("My example")
    @Example("A foo example")
    private String summary;

    @Property("Description")
    @Hint("My description")
    @Example("A foo description")
    private String description;

    @Property("External Value")
    @Hint("http://example.org/examples/address-example.xml")
    @Example("http://example.org/examples/address-example.xml")
    private String externalValue;

    @Property("Example")
    @WidthAuto
    @Hint("assets/get-orders-example.json")
    @Example("assets/get-orders-example.json")
    @HintBrowseFile("Select Example File ...")
    @Description("The path and name of the example to be read from the project's resources folder.")
    private ResourceText value;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalValue() {
        return externalValue;
    }

    public void setExternalValue(String externalValue) {
        this.externalValue = externalValue;
    }

    public ResourceText getValue() {
        return value;
    }

    public void setValue(ResourceText value) {
        this.value = value;
    }

    @Override
    public com.reedelk.openapi.v3.model.ExampleObject map(OpenApiSerializableContext context) {
        com.reedelk.openapi.v3.model.ExampleObject mapped =
                new com.reedelk.openapi.v3.model.ExampleObject();
        mapped.setSummary(summary);
        mapped.setDescription(description);
        mapped.setExternalValue(externalValue);
        if (value != null) {
            String exampleData = StreamUtils.FromString.consume(value.data());
            mapped.setValue(exampleData);
        }
        return mapped;
    }
}
