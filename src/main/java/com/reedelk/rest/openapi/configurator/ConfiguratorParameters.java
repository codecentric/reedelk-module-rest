package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.paths.ParameterObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfiguratorParameters implements Configurator {

    @Override
    public void configure(OpenAPI api, RestMethod method, OperationObject configuration, com.reedelk.rest.openapi.paths.OperationObject operationObject) {
        Optional.ofNullable(configuration.getParameters()).ifPresent(openApiParameters -> {
            List<ParameterObject> parameterObjectList = new ArrayList<>();
            operationObject.setParameters(parameterObjectList);

            /**
            openApiParameters.getParameters().forEach((parameterName, paramDefinition) -> {
                ParameterObject parameterObject = new ParameterObject();
                parameterObject.setName(parameterName);
                parameterObject.setRequired(paramDefinition.getRequired());
                parameterObject.setDeprecated(paramDefinition.getDeprecated());
                parameterObject.setDescription(paramDefinition.getDescription());
                parameterObject.setIn(Optional.ofNullable(paramDefinition.getIn()).orElse(ParameterLocation.PATH).value());
                parameterObjectList.add(parameterObject);
            });*/
        });
    }
}
