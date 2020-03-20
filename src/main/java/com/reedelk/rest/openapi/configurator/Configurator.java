package com.reedelk.rest.openapi.configurator;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.rest.openapi.OpenAPI;
import com.reedelk.rest.openapi.paths.OperationObject;

/**
 * An interface which configures OpenAPI object from given OpenApi configuration object.
 * The OpenApi configuration object is given by the user.
 */
public interface Configurator {

    void configure(OpenAPI api, RestMethod method, OpenApiConfiguration configuration, OperationObject operationObject);
}
