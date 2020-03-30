package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.listener.openapi.MediaTypeObject;
import com.reedelk.rest.component.listener.openapi.OpenApiObject;
import com.reedelk.rest.component.listener.openapi.OperationObject;
import com.reedelk.rest.component.listener.openapi.ResponseObject;
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
    private OpenApiObject openAPI = new OpenApiObject();

    @BeforeEach
    void setUp() {
        handler = new TestableOpenAPIRequestHandler(openAPI);
    }

    @Test
    void shouldExcludeResourcePathFromOpenApi() {
        // Given
        OperationObject configuration = new OperationObject();
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

        MediaTypeObject response200 = new MediaTypeObject();
        response200.setDescription("200 Response");
        response200.setExample(response200Example);
        response200.setMediaType(APPLICATION_JSON.toString());

        MediaTypeObject response400 = new MediaTypeObject();
        response400.setExample(response400Example);
        response400.setMediaType(APPLICATION_JSON.toString());

        Map<String, MediaTypeObject> responses =
                ImmutableMap.of("200", response200, "400", response400);

        ResponseObject response = new ResponseObject();
        response.setContent(responses);

        OperationObject configuration = new OperationObject();
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

        protected TestableOpenAPIRequestHandler(OpenApiObject openAPI) {
            super(null); // TODO: Fixme
            this.openAPI = openAPI;
        }
    }
}
