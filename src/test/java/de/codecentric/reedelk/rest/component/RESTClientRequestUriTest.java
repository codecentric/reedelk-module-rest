package de.codecentric.reedelk.rest.component;

import com.github.tomakehurst.wiremock.client.WireMock;
import de.codecentric.reedelk.rest.TestComponent;
import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static de.codecentric.reedelk.rest.internal.commons.RestMethod.valueOf;
import static de.codecentric.reedelk.runtime.api.commons.ImmutableMap.of;


class RESTClientRequestUriTest extends RESTClientAbstractTest {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldCorrectlyBuildRequestUriWithPathParams(String method) {
        // Given
        String path = "/resource/{id}/group/{group}";
        String expectedPath = "/resource/aabbccddeeff/group/user";

        DynamicStringMap pathParameters = DynamicStringMap.from(of(
                "id", "aabbccddeeff",
                "group", "user"), moduleContext);

        // Expect
        assertExpectedPath(method, path, expectedPath, pathParameters, null);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldCorrectlyBuildRequestUriWithQueryParams(String method) {
        // Given
        String path = "/resource";
        String expectedPath = "/resource?query1=value1&query2=value2";

        DynamicStringMap queryParameters = DynamicStringMap.from(of(
                "query1", "value1",
                "query2", "value2"), moduleContext);

        // Expect
        assertExpectedPath(method, path, expectedPath, null, queryParameters);
    }

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"})
    void shouldCorrectlyBuildRequestUriWithPathAndQueryParams(String method) {
        // Given
        String path = "/resource/{id}/title/{title}";
        String expectedPath = "/resource/aabb1122/title/manager?query1=value1&query2=value2";

        DynamicStringMap queryParameters = DynamicStringMap.from(of("query1", "value1", "query2", "value2"), moduleContext);
        DynamicStringMap pathParameters = DynamicStringMap.from(of("id", "aabb1122", "title", "manager"), moduleContext);

        // Expect
        assertExpectedPath(method, path, expectedPath, pathParameters, queryParameters);
    }

    void assertExpectedPath(String method, String path, String expectedPath, DynamicStringMap pathParameters, DynamicStringMap queryParameters) {
        // Given
        givenThat(WireMock.any(urlEqualTo(expectedPath)).willReturn(aResponse().withStatus(200)));

        Message message = MessageBuilder.get(TestComponent.class).empty().build();
        RestMethod restMethod = RestMethod.valueOf(method);
        RESTClient restClient = clientWith(restMethod, BASE_URL, path, pathParameters, queryParameters);

        // Expect
        AssertHttpResponse.isSuccessful(restClient, message, flowContext);
    }
}
