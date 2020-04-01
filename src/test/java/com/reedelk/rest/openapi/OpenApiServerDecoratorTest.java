package com.reedelk.rest.openapi;

import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.component.RestListenerConfiguration;
import com.reedelk.rest.server.HttpRequestHandler;
import com.reedelk.rest.server.HttpRouteHandler;
import com.reedelk.rest.server.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.reedelk.rest.commons.RestMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenApiServerDecoratorTest {

    @Mock
    private Server delegate;
    @Mock
    private HttpRouteHandler openApiHandler;
    @Mock
    private HttpRequestHandler requestHandler;

    @Test
    void shouldAddOpenApiRouteToDelegate() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();

        // When
        new OpenApiServerDecorator(configuration, delegate);

        // Then
        verify(delegate).addRoute(eq("/openapi.json"),
                eq(GET),
                eq(null),
                eq(null),
                eq(null),
                any(OpenApiRequestHandler.class));
    }

    @Test
    void shouldReturnHasEmptyRoutesTrue() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        doReturn(Collections.singletonList(openApiHandler)).when(delegate).handlers();

        // When
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // Then
        assertThat(decorator.hasEmptyRoutes()).isTrue();
    }

    @Test
    void shouldAddRouteToDelegate() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // When
        decorator.addRoute("/",
                RestMethod.POST,
                null,
                null,
                null,
                requestHandler);

        // Then
        verify(delegate).addRoute("/",
                RestMethod.POST,
                null,
                null,
                null,
                requestHandler);
    }

    @Test
    void shouldRemoveRouteFromDelegate() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // When
        decorator.removeRoute("/", RestMethod.POST);

        // Then
        verify(delegate).removeRoute("/", RestMethod.POST);
    }

    @Test
    void shouldStopRemoveOpenApiRouteFromDelegateAndStop() {
        // Given
        RestListenerConfiguration configuration = new RestListenerConfiguration();
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // When
        decorator.stop();

        // Then
        verify(delegate).removeRoute("/openapi.json", GET);
        verify(delegate).stop();
    }
}
