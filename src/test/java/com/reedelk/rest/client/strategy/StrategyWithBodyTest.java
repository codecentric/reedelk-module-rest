package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.body.BodyResult;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.runtime.api.commons.ImmutableMap;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StrategyWithBodyTest {

    @Mock
    private RequestWithBodyFactory factory;
    @Mock
    private BodyResult bodyResult;

    final int responseBufferSize = 1024 * 2;

    @Test
    void shouldNotAddContentTypeToHttpEntityWhenMultipart() {
        // Given
        StrategyWithBody strategy = new StrategyWithBody(factory, responseBufferSize);
        HeaderProvider headerProvider = () -> ImmutableMap.of(
                "Content-TypE", "text/plain",
                "Accept", "application/json");

        doReturn(true)
                .when(bodyResult)
                .isMultipart();

        HttpPost httpPost = new HttpPost();

        // When
        strategy.addHttpHeaders(headerProvider, bodyResult, httpPost);

        // Then
        assertThat(httpPost.containsHeader("Content-Type")).isFalse();
        assertThat(httpPost.containsHeader("AccepT")).isTrue();

        Header acceptHeader = httpPost.getFirstHeader("Accept");
        assertThat(acceptHeader.getValue()).isEqualTo("application/json");
    }

    @Test
    void shouldAddContentTypeToHttpEntityWhenNotMultipart() {
        // Given
        StrategyWithBody strategy = new StrategyWithBody(factory, responseBufferSize);
        HeaderProvider headerProvider = () -> ImmutableMap.of(
                "Content-TypE", "text/plain",
                "Accept", "application/json");

        doReturn(false)
                .when(bodyResult)
                .isMultipart();

        HttpPost httpPost = new HttpPost();

        // When
        strategy.addHttpHeaders(headerProvider, bodyResult, httpPost);

        // Then
        assertThat(httpPost.containsHeader("Content-Type")).isTrue();

        Header header = httpPost.getFirstHeader("Content-type");
        assertThat(header.getValue()).isEqualTo("text/plain");

        Header acceptHeader = httpPost.getFirstHeader("Accept");
        assertThat(acceptHeader.getValue()).isEqualTo("application/json");
    }
}
