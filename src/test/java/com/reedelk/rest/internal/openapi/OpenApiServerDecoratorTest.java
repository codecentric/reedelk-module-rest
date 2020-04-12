package com.reedelk.rest.internal.openapi;

import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.component.RestListener1Configuration;
import com.reedelk.rest.internal.server.HttpRequestHandler;
import com.reedelk.rest.internal.server.HttpRouteHandler;
import com.reedelk.rest.internal.server.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.reedelk.rest.internal.commons.RestMethod.GET;
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
        RestListener1Configuration configuration = new RestListener1Configuration();

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
        RestListener1Configuration configuration = new RestListener1Configuration();
        doReturn(Collections.singletonList(openApiHandler)).when(delegate).handlers();

        // When
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // Then
        assertThat(decorator.hasEmptyRoutes()).isTrue();
    }

    @Test
    void shouldAddRouteToDelegate() {
        // Given
        RestListener1Configuration configuration = new RestListener1Configuration();
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
        RestListener1Configuration configuration = new RestListener1Configuration();
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // When
        decorator.removeRoute("/", RestMethod.POST);

        // Then
        verify(delegate).removeRoute("/", RestMethod.POST);
    }

    @Test
    void shouldStopRemoveOpenApiRouteFromDelegateAndStop() {
        // Given
        RestListener1Configuration configuration = new RestListener1Configuration();
        OpenApiServerDecorator decorator = new OpenApiServerDecorator(configuration, delegate);

        // When
        decorator.stop();

        // Then
        verify(delegate).removeRoute("/openapi.json", GET);
        verify(delegate).stop();
    }
}
