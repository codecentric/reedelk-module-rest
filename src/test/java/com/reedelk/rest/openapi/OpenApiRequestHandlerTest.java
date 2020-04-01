package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.RestListenerConfiguration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.rest.component.listener.openapi.AbstractOpenApiSerializableTest;
import com.reedelk.rest.component.listener.openapi.OpenApiJsons;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicInteger;
import org.junit.jupiter.api.Test;

class OpenApiRequestHandlerTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldSerializeCorrectlyEmptyOpenApi() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, OpenApiJsons.Examples.OpenApiEmpty);
    }

    // One response with default
    @Test
    void shouldSerializeWithPathAndMethod() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);
        handler.add("/", RestMethod.GET, null, null, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, OpenApiJsons.Examples.OpenApiWithPathAndMethod);
    }

    @Test
    void shouldSerializeWithPathAndMethodAndResponseHeaders() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        Response response = new Response();
        response.setStatus(DynamicInteger.from(200));
        DynamicStringMap responseHeaders =
                DynamicStringMap.from(ImmutableMap.of("header1", "'my header'"), new ModuleContext(10L));
        response.setHeaders(responseHeaders);

        handler.add("/", RestMethod.GET, response, null, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, OpenApiJsons.Examples.OpenApiWithPathAndMethodAndHeaders);
    }

    @Test
    void shouldSerializeWithPathAndMethodAndResponseErrorHeaders() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(DynamicInteger.from(500));
        DynamicStringMap responseHeaders =
                DynamicStringMap.from(ImmutableMap.of("header1", "'my header'"), new ModuleContext(10L));
        errorResponse.setHeaders(responseHeaders);

        handler.add("/", RestMethod.GET, null, errorResponse, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, OpenApiJsons.Examples.OpenApiWithPathAndMethodAndHeadersError);
    }

    @Test
    void shouldSerializeWithPathParamsFromPath() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);
        Response response = new Response();
        response.setStatus(DynamicInteger.from(200));

        handler.add("/resource/{id}/{group}", RestMethod.GET, response, null, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, OpenApiJsons.Examples.OpenApiWithPathParams);
    }
}
