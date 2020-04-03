package com.reedelk.rest.internal.commons;

import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTypeTest {

    @Test
    void shouldReturnCorrectContentType() {
        // Given
        Message messageWithJson = MessageBuilder.get().withJson("{}").build();

        // When
        Optional<String> maybeContentType = ContentType.from(messageWithJson);

        // Then
        assertThat(maybeContentType).isPresent();
    }

    @Test
    void shouldReturnEmptyWhenMessageHasEmptyContent() {
        // Given
        Message messageWithNullContent = MessageBuilder.get().empty().build();

        // When
        Optional<String> maybeContentType = ContentType.from(messageWithNullContent);

        // Then
        assertThat(maybeContentType).isEmpty();
    }

    @Test
    void shouldReturnEmptyMimeTypeWhenEmptyContent() {
        // Given
        Message messageWithNullContent = MessageBuilder.get().empty(MimeType.TEXT).build();

        // When
        Optional<String> maybeContentType = ContentType.from(messageWithNullContent);

        // Then
        assertThat(maybeContentType).hasValue("text/plain");
    }

    @Test
    void shouldReturnEmptyWhenMessageContentHasNullMimeType() {
        // Given
        Message message = MessageBuilder.get().withString("test", null).build();

        // When
        Optional<String> maybeContentType = ContentType.from(message);

        // Then
        assertThat(maybeContentType).isNotPresent();
    }
}
