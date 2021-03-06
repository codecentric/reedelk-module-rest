package de.codecentric.reedelk.rest.internal.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.netty.http.server.HttpServerRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DefaultServerRoutesTest {

    @Mock
    private HttpRouteHandler handler1;
    @Mock
    private HttpRouteHandler handler2;
    @Mock
    private HttpRouteHandler handler3;

    @Mock
    private HttpServerRequest request;

    @Test
    void shouldReturnExactMatchHandler() {
        // Given
        Mockito.doReturn(HttpPredicate.MatcherResult.TEMPLATE_MATCH).when(handler1).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler2).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.EXACT_MATCH).when(handler3).matches(request);

        List<HttpRouteHandler> handlerList = Arrays.asList(handler1, handler2, handler3);

        // When
        Optional<HttpRouteHandler> actual =
                DefaultServerRoutes.findMatchingHttpRouteHandler(handlerList, request);

        // Then
        assertThat(actual).isPresent().contains(handler3);
    }

    @Test
    void shouldReturnTemplateMatchHandler() {
        // Given
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler1).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.TEMPLATE_MATCH).when(handler2).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler3).matches(request);

        List<HttpRouteHandler> handlerList = Arrays.asList(handler1, handler2, handler3);

        // When
        Optional<HttpRouteHandler> actual =
                DefaultServerRoutes.findMatchingHttpRouteHandler(handlerList, request);

        // Then
        assertThat(actual).isPresent().contains(handler2);
    }

    @Test
    void shouldReturnEmptyHandler() {
        // Given
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler1).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler2).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler3).matches(request);

        List<HttpRouteHandler> handlerList = Arrays.asList(handler1, handler2, handler3);

        // When
        Optional<HttpRouteHandler> actual =
                DefaultServerRoutes.findMatchingHttpRouteHandler(handlerList, request);

        // Then
        assertThat(actual).isNotPresent();
    }

    @Test
    void shouldReturnExactMatchHandlerWhenOnlyOneMatching() {
        // Given
        Mockito.doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler1).matches(request);
        Mockito.doReturn(HttpPredicate.MatcherResult.EXACT_MATCH).when(handler2).matches(request);
        lenient().doReturn(HttpPredicate.MatcherResult.NO_MATCH).when(handler3).matches(request);

        List<HttpRouteHandler> handlerList = Arrays.asList(handler1, handler2, handler3);

        // When
        Optional<HttpRouteHandler> actual =
                DefaultServerRoutes.findMatchingHttpRouteHandler(handlerList, request);

        // Then
        assertThat(actual).isPresent().contains(handler2);
    }
}
