package com.reedelk.rest.component.listener.openapi.v3.model;

/**
 * An interface used to map an object to OpenAPI Model.
 * @param <T> the mapped OpenAPI model type.
 */
public interface OpenAPIModel<T> {

    T map();
}
