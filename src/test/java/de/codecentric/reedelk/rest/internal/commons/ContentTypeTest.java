package de.codecentric.reedelk.rest.internal.commons;

import de.codecentric.reedelk.runtime.api.component.ProcessorSync;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ContentTypeTest {

    @Test
    void shouldReturnCorrectContentType() {
        // Given
        Message messageWithJson = MessageBuilder.get(MyTestComponent.class).withJson("{}").build();

        // When
        Optional<String> maybeContentType = ContentType.from(messageWithJson);

        // Then
        assertThat(maybeContentType).isPresent();
    }

    @Test
    void shouldReturnEmptyWhenMessageHasEmptyContent() {
        // Given
        Message messageWithNullContent = MessageBuilder.get(MyTestComponent.class).empty().build();

        // When
        Optional<String> maybeContentType = ContentType.from(messageWithNullContent);

        // Then
        assertThat(maybeContentType).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenMessageContentHasNullMimeType() {
        // Given
        Message message = MessageBuilder.get(MyTestComponent.class).withString("test", null).build();

        // When
        Optional<String> maybeContentType = ContentType.from(message);

        // Then
        assertThat(maybeContentType).isNotPresent();
    }

    static class MyTestComponent implements ProcessorSync {
        @Override
        public Message apply(FlowContext flowContext, Message message) {
            throw new UnsupportedOperationException();
        }
    }
}
