package com.reedelk.rest.component.listener.openapi.v3;

import com.reedelk.openapi.OpenApiSerializableContext;

/**
 * An interface used to map an object to OpenAPI Model.
 * @param <T> the mapped OpenAPI model type.
 */
public interface OpenAPIModel<T> {

    T map(OpenApiSerializableContext context);

}
