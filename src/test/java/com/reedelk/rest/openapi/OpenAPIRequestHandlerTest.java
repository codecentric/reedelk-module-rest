package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.openapi.OpenApiConfiguration;
import com.reedelk.rest.component.listener.openapi.OpenApiResponse;
import com.reedelk.rest.component.listener.openapi.OpenApiResponseDefinition;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.reedelk.runtime.api.message.content.MimeType.APPLICATION_JSON;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

@ExtendWith(MockitoExtension.class)
class OpenAPIRequestHandlerTest {

    private TestableOpenAPIRequestHandler handler;
    private OpenAPI openAPI = new OpenAPI();

    @BeforeEach
    void setUp() {
        handler = new TestableOpenAPIRequestHandler(openAPI);
    }

    @Test
    void shouldExcludeResourcePathFromOpenApi() {
        // Given
        OpenApiConfiguration configuration = new OpenApiConfiguration();
        configuration.setExclude(true);

        // When
        handler.add("/resource", RestMethod.GET, configuration);

        // Then
        JSONObject serialized = openAPI.serialize();
        String actual = serialized.toString(4);

        // We expect /resource not present in the open API specification.
        assertEquals(JSONS.EmptyOpenAPI.string(), actual, STRICT);
    }

    @Test
    void shouldCorrectlySerializeResponse() {
        // Given
        ResourceText response200Example = mock(ResourceText.class);
        ResourceText response400Example = mock(ResourceText.class);
        doReturn(Mono.just("{ \"item\": \"Item 1\" }")).when(response200Example).data();
        doReturn(Mono.just("{ \"error\": \"Error message\" }")).when(response400Example).data();

        OpenApiResponseDefinition response200 = new OpenApiResponseDefinition();
        response200.setDescription("200 Response");
        response200.setExample(response200Example);
        response200.setMediaType(APPLICATION_JSON.toString());

        OpenApiResponseDefinition response400 = new OpenApiResponseDefinition();
        response400.setExample(response400Example);
        response400.setMediaType(APPLICATION_JSON.toString());

        Map<String, OpenApiResponseDefinition> responses =
                ImmutableMap.of("200", response200, "400", response400);

        OpenApiResponse response = new OpenApiResponse();
        response.setResponses(responses);

        OpenApiConfiguration configuration = new OpenApiConfiguration();
        configuration.setResponse(response);

        // When
        handler.add("/resource", RestMethod.GET, configuration);

        // Then
        JSONObject serialized = openAPI.serialize();
        String actual = serialized.toString(4);

        // We expect /resource not present in the open API specification.
        assertEquals(JSONS.MultipleResponsesForPath.string(), actual, STRICT);
    }

    static class TestableOpenAPIRequestHandler extends OpenAPIRequestHandler {

        protected TestableOpenAPIRequestHandler(OpenAPI openAPI) {
            super(null); // TODO: Fixme
            this.openAPI = openAPI;
        }
    }
}
