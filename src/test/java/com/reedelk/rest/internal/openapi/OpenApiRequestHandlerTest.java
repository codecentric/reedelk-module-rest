package com.reedelk.rest.internal.openapi;

import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.component.listener.ErrorResponse;
import com.reedelk.rest.component.listener.Response;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.resource.ResourceText;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicInteger;
import com.reedelk.runtime.openapi.v3.PredefinedSchema;
import com.reedelk.runtime.openapi.v3.model.*;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static reactor.core.publisher.Mono.just;

class OpenApiRequestHandlerTest {

    @Test
    void shouldSerializeCorrectlyEmptyOpenApi() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiEmpty);
    }

    @Test
    void shouldCorrectlySetBasePath() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        configuration.setBasePath("/api/v1");
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiCustomBasePath);
    }

    // One response with default
    @Test
    void shouldSerializeWithPathAndMethod() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);
        handler.add("/", RestMethod.GET, null, null, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiWithPathAndMethod);
    }

    @Test
    void shouldSerializeWithPathAndMethodAndResponseHeaders() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
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
        assertSerializedCorrectly(serialized, Examples.OpenApiWithPathAndMethodAndHeaders);
    }

    @Test
    void shouldSerializeWithPathAndMethodAndResponseErrorHeaders() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
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
        assertSerializedCorrectly(serialized, Examples.OpenApiWithPathAndMethodAndHeadersError);
    }

    @Test
    void shouldSerializeWithPathParamsFromPath() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);
        Response response = new Response();
        response.setStatus(DynamicInteger.from(200));

        handler.add("/resource/{id}/{group}", RestMethod.GET, response, null, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiWithPathParams);
    }

    @Test
    void shouldSerializeOverridePathParamDefinitionFromOperation() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);
        Response response = new Response();
        response.setStatus(DynamicInteger.from(200));

        ParameterObject idParam = new ParameterObject();
        idParam.setName("id");
        idParam.setIn(ParameterLocation.path);
        idParam.setStyle(ParameterStyle.simple);
        idParam.setDescription("This is my custom param");
        idParam.setExample("This is an example");
        idParam.setPredefinedSchema(PredefinedSchema.INTEGER);

        OperationObject operation = new OperationObject();
        operation.getParameters().add(idParam); // we override id param

        handler.add("/resource/{id}/{group}", RestMethod.GET, response, null, operation);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiWithOverriddenPathParams);
    }

    @Test
    void shouldSerializeExcludePathFromOpenApi() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        OperationObject operation = new OperationObject();
        operation.setExclude(true);

        handler.add("/resource/{id}/{group}", RestMethod.GET, null, null, operation);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiEmpty);
    }

    @Test
    void shouldSerializeRemovePathCorrectly() {
        // Given
        String path = "/";

        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);
        handler.add(path, RestMethod.GET, null, null, null);

        // When
        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiWithPathAndMethod);

        // When
        handler.remove(path, RestMethod.GET);
        serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiEmpty);
    }

    @Test
    void shouldSerializeCustomSchemasCorrectly() {
        // Given
        RESTListenerConfiguration configuration = new RESTListenerConfiguration();
        OpenApiRequestHandler handler = new OpenApiRequestHandler(configuration);

        ResourceText petSchemaResource = mock(ResourceText.class);
        doReturn(just(Schemas.Pet.string())).when(petSchemaResource).data();
        SchemaObject petSchema = new SchemaObject();
        petSchema.setSchema(petSchemaResource);

        ResourceText coordinatesSchemaResource = mock(ResourceText.class);
        doReturn(just(Schemas.Coordinates.string())).when(coordinatesSchemaResource).data();
        SchemaObject coordinatesSchema = new SchemaObject();
        coordinatesSchema.setSchema(coordinatesSchemaResource);

        ComponentsObject components = configuration.getOpenApi().getComponents();
        components.getSchemas().put("Pet", petSchema);
        components.getSchemas().put("Coordinates", coordinatesSchema);

        handler.add("/", RestMethod.GET, null, null, null);

        String serialized = handler.serializeOpenApi();

        // Then
        assertSerializedCorrectly(serialized, Examples.OpenApiWithSchemas);
    }

    protected void assertSerializedCorrectly(String actual, JsonProvider expected) {
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actual, JSONCompareMode.STRICT);
    }
}
