package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.openapi.OpenAPI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OpenApiConfigurator {

    private static final List<Configurator> CONFIGURATORS =
            Collections.unmodifiableList(Arrays.asList(
                    new ConfiguratorParameters(),
                    new ConfiguratorResponse(),
                    new ConfiguratorRequest()));

    public static com.reedelk.rest.openapi.paths.OperationObject configure(OpenAPI openAPI, RestMethod method, OperationObject openApiConfiguration) {
        com.reedelk.rest.openapi.paths.OperationObject operationObject = new com.reedelk.rest.openapi.paths.OperationObject();
        operationObject.setOperationId(openApiConfiguration.getOperationId());
        operationObject.setDescription(openApiConfiguration.getDescription());
        operationObject.setSummary(openApiConfiguration.getSummary());

        CONFIGURATORS.forEach(configurator ->
                configurator.configure(openAPI, method, openApiConfiguration, operationObject));

        return operationObject;
    }
}
