package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.HttpClientResultCallback;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.body.BodyResult;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.rest.client.response.BufferSizeAwareResponseConsumer;
import com.reedelk.rest.commons.HttpHeader;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Parts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import java.net.URI;
import java.util.concurrent.Future;

/**
 * No streaming strategy. Content-Length header is sent.
 */
public class StrategyWithBody implements Strategy {

    private final int responseBufferSize;
    private final RequestWithBodyFactory requestFactory;

    StrategyWithBody(RequestWithBodyFactory requestFactory, int responseBufferSize) {
        this.requestFactory = requestFactory;
        this.responseBufferSize = responseBufferSize;
    }

    @Override
    public Future<HttpResponse> execute(HttpClient client,
                                        Message input,
                                        FlowContext flowContext,
                                        URI uri,
                                        HeaderProvider headerProvider,
                                        BodyProvider bodyProvider,
                                        HttpClientResultCallback callback) {

        BodyResult bodyResult = bodyProvider.asByteArray(input, flowContext);
        HttpEntity entity = createHttpEntity(bodyResult);

        HttpEntityEnclosingRequestBase request = requestFactory.create();
        request.setURI(uri);
        request.setEntity(entity);

        addHttpHeaders(headerProvider, bodyResult, request);

        HttpAsyncRequestProducer requestProducer = HttpAsyncMethods.create(request);
        HttpAsyncResponseConsumer<HttpResponse> responseConsumer = BufferSizeAwareResponseConsumer.createConsumer(responseBufferSize);
        return client.execute(requestProducer, responseConsumer, callback);
    }

    private HttpEntity createHttpEntity(BodyResult bodyResult) {
        if (bodyResult.isMultipart()) {
            Parts dataAsMultipart = bodyResult.getDataAsMultipart();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            dataAsMultipart.forEach((partName, part) -> {

                // TODO: Here need to support sending bytes or string and so on....
                byte[] dataAsBytes = (byte[]) part.getContent().data();
                String filename = part.getAttributes().get("filename");
                builder.addBinaryBody(partName, dataAsBytes, ContentType.APPLICATION_OCTET_STREAM, filename);
            });
            return new MultipartFormEntityWrapper(builder.build());
        } else {
            byte[] body = bodyResult.getDataAsBytes();
            return new NByteArrayEntity(body);
        }
    }

    private void addHttpHeaders(HeaderProvider headerProvider, BodyResult bodyResult, HttpEntityEnclosingRequestBase request) {
        headerProvider.headers().forEach((headerName, headerValue) -> {
            // If multipart, the content type is set by the underlying client.
            if (HttpHeader.CONTENT_TYPE.equalsIgnoreCase(headerName)) {
                // If it is not multipart, we add the content type header, otherwise
                // the content type is set by the apache http entity.
                if (!bodyResult.isMultipart()) {
                    request.addHeader(headerName, headerValue);
                }
            } else {
                request.addHeader(headerName, headerValue);
            }
        });
    }
}
