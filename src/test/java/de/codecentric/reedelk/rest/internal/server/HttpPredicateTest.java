package de.codecentric.reedelk.rest.internal.server;

import io.netty.handler.codec.http.HttpMethod;
import org.junit.jupiter.api.Test;

import static de.codecentric.reedelk.rest.internal.server.HttpPredicate.MatcherResult;
import static org.assertj.core.api.Assertions.assertThat;

class HttpPredicateTest {

    @Test
    void shouldReturnNoMatchWhenUriIsDifferent() {
        // Given
        HttpPredicate predicate = new HttpPredicate("/test", HttpMethod.GET);

        // When
        MatcherResult result = predicate.matches(HttpMethod.GET, "/test/something");

        // Then
        assertThat(result).isEqualTo(MatcherResult.NO_MATCH);
    }

    @Test
    void shouldReturnNoMatchWhenMethodIsDifferent() {
        // Given
        HttpPredicate predicate = new HttpPredicate("/test", HttpMethod.GET);

        // When
        MatcherResult result = predicate.matches(HttpMethod.POST, "/test");

        // Then
        assertThat(result).isEqualTo(MatcherResult.NO_MATCH);
    }

    @Test
    void shouldReturnExactMatchWhenRequestUri() {
        // Given
        HttpPredicate predicate = new HttpPredicate("/test", HttpMethod.GET);

        // When
        MatcherResult result = predicate.matches(HttpMethod.GET, "/test");

        // Then
        assertThat(result).isEqualTo(MatcherResult.EXACT_MATCH);
    }

    @Test
    void shouldReturnExactMatchWhenRequestUriWithQueryParams() {
        // Given
        HttpPredicate predicate = new HttpPredicate("/test", HttpMethod.GET);

        // When
        MatcherResult result = predicate.matches(HttpMethod.GET, "/test?param1=value1&param2=value2");

        // Then
        assertThat(result).isEqualTo(MatcherResult.EXACT_MATCH);
    }

    @Test
    void shouldReturnTemplateMatch() {
        // Given
        HttpPredicate predicate = new HttpPredicate("/test/{id}/{group}", HttpMethod.GET);

        // When
        MatcherResult result = predicate.matches(HttpMethod.GET, "/test/12/manager");

        // Then
        assertThat(result).isEqualTo(MatcherResult.TEMPLATE_MATCH);
    }

    @Test
    void shouldReturnTemplateMatchWithQueryParams() {
        // Given
        HttpPredicate predicate = new HttpPredicate("/test/{id}/{group}", HttpMethod.POST);

        // When
        MatcherResult result = predicate.matches(HttpMethod.POST, "/test/12/manager?param1=value1&param2=value2");

        // Then
        assertThat(result).isEqualTo(MatcherResult.TEMPLATE_MATCH);
    }
}
