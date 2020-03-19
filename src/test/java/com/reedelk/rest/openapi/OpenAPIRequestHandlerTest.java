package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.OpenApiConfiguration;
import com.reedelk.runtime.api.resource.ResourceText;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OpenAPIRequestHandlerTest {

    @Mock
    private ResourceText responseExample;

    private TestableOpenAPIRequestHandler handler;
    private OpenAPI openAPI = new OpenAPI();

    @BeforeEach
    void setUp() {
        handler = new TestableOpenAPIRequestHandler(openAPI);
        doReturn(Mono.just("{ \"name\": \"Mark\" }"))
                .when(responseExample).data();
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
        JSONAssert.assertEquals(JSONS.EmptyOpenAPI.string(), actual, JSONCompareMode.STRICT);
    }

    @Test
    void shouldDoSomething() {
        /**
        // Given
        OpenApiResponse response200 = new OpenApiResponse();
        response200.setExample(responseExample);
        response200.setMediaType("application/json");

        Map<String, OpenApiResponse> responses = new HashMap<>();
        responses.put("200", response200);

        OpenApiConfiguration configuration = new OpenApiConfiguration();
        configuration.setResponses(responses);

        // When
        handler.add("/resource", RestMethod.GET, configuration);

        // Then
        JSONObject serialized = openAPI.serialize();
        String actual = serialized.toString(4);

        // We expect /resource not present in the open API specification.
        JSONAssert.assertEquals(JSONS.EmptyOpenAPI.string(), actual, JSONCompareMode.STRICT);
         */
    }

    static class TestableOpenAPIRequestHandler extends OpenAPIRequestHandler {

        protected TestableOpenAPIRequestHandler(OpenAPI openAPI) {
            this.openAPI = openAPI;
        }
    }
}
