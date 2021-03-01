package de.codecentric.reedelk.rest.internal.server.mapper;

import de.codecentric.reedelk.rest.internal.attribute.RESTListenerAttributes;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.message.MessageBuilder;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import reactor.netty.http.server.HttpServerRequest;

import static de.codecentric.reedelk.runtime.api.message.content.MimeType.MULTIPART_FORM_DATA;

public class HttpRequestMessageMapper {

    private final String matchingPath;

    public HttpRequestMessageMapper(String matchingPath) {
        this.matchingPath = matchingPath;
    }

    public Message map(HttpServerRequest httpRequest) {
        HttpRequestWrapper request = new HttpRequestWrapper(httpRequest);

        MimeType mimeType = request.mimeType();

        MessageBuilder messageBuilder = MULTIPART_FORM_DATA.equals(mimeType) ?
                HttpRequestMultipartFormDataMapper.map(request) :
                HttpRequestContentMapper.map(request);

        RESTListenerAttributes attributes = new RESTListenerAttributes(request, matchingPath);

        return messageBuilder
                .attributes(attributes)
                .build();
    }
}
