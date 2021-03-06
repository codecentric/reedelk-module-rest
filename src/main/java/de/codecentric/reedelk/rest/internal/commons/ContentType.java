package de.codecentric.reedelk.rest.internal.commons;

import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import de.codecentric.reedelk.runtime.api.message.content.TypedContent;

import java.util.Optional;

public class ContentType {

    private ContentType() {
    }

    public static Optional<String> from(Message message) {
        TypedContent<?, ?> content = message.content();
        return Optional.ofNullable(content)
                .flatMap(typedContent -> Optional.ofNullable(typedContent.mimeType()))
                .map(MimeType::toString);
    }
}
