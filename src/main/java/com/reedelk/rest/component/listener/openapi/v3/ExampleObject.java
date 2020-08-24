package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.resource.ResourceText;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = ExampleObject.class, scope = ServiceScope.PROTOTYPE)
public class ExampleObject implements Implementor {

    @Property("Example")
    @WidthAuto
    @Hint("assets/get-orders-example.json")
    @Example("assets/get-orders-example.json")
    @HintBrowseFile("Select Example File ...")
    @Description("The path and name of the example to be read from the project's resources folder.")
    private ResourceText example;

    public ResourceText getExample() {
        return example;
    }

    public void setExample(ResourceText example) {
        this.example = example;
    }
}
