package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.paths.OperationObject;
import com.reedelk.rest.openapi.paths.ParameterObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfiguratorParameters implements Configurator {

    @Override
    public void configure(OpenAPI api, OpenApiConfiguration configuration, OperationObject operationObject) {
        Optional.ofNullable(configuration.getParameters()).ifPresent(openApiParameters -> {
            List<ParameterObject> parameterObjectList = new ArrayList<>();
            operationObject.setParameters(parameterObjectList);

            openApiParameters.getParameters().forEach((parameterName, openApiParameterDefinition) -> {
                ParameterObject parameterObject = new ParameterObject();
                parameterObject.setDeprecated(openApiParameterDefinition.getDeprecated());
                parameterObject.setDescription(openApiParameterDefinition.getDescription());
                parameterObject.setRequired(openApiParameterDefinition.getRequired());
                parameterObject.setIn(openApiParameterDefinition.getIn().name());
                parameterObject.setName(parameterName);
                parameterObjectList.add(parameterObject);
            });
        });
    }
}
