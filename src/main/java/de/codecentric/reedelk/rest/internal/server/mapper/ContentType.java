package de.codecentric.reedelk.rest.internal.server.mapper;

import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;

import java.util.Optional;

public class ContentType {

    public static Optional<String> from(DynamicByteArray responseBody, Message message) {
        // If it is a script and the script is evaluate the payload, then we set the
        // content type from the payload's mime type.
        if (ScriptUtils.isEvaluateMessagePayload(responseBody)) {
            return de.codecentric.reedelk.rest.internal.commons.ContentType.from(message);
        } else {
            // Otherwise we get the content type from the custom headers.
            return Optional.empty();
        }
    }
}
