package com.reedelk.rest.client.strategy;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.HttpClientResultCallback;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.rest.commons.HttpHeader;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Parts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.entity.NByteArrayEntity;

import java.io.*;
import java.net.URI;
import java.util.concurrent.Future;

/**
 * No streaming strategy. Content-Length header is sent.
 */
public class StrategyWithBody implements Strategy {

    private final int responseBufferSize;
    private final RequestWithBodyFactory requestFactory;
    private final Boolean multipart;

    StrategyWithBody(RequestWithBodyFactory requestFactory, int responseBufferSize, Boolean multipart) {
        this.requestFactory = requestFactory;
        this.responseBufferSize = responseBufferSize;
        this.multipart = multipart;
    }

    @Override
    public Future<HttpResponse> execute(HttpClient client,
                                        Message input,
                                        FlowContext flowContext,
                                        URI uri,
                                        HeaderProvider headerProvider,
                                        BodyProvider bodyProvider,
                                        HttpClientResultCallback callback) {




        HttpEntity entity;
        if (multipart != null && multipart) {
            Parts parts = bodyProvider.asParts(input, flowContext);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            parts.forEach((partName, part) -> {
                byte[] dataAsBytes = (byte[]) part.getContent().data();
                String filename = part.getAttributes().get("filename");
                builder.addBinaryBody(partName, dataAsBytes, ContentType.APPLICATION_OCTET_STREAM, filename);
            });
            entity = new HttpEntityWrapper(builder.build());

        } else {
            byte[] body = bodyProvider.asByteArray(input, flowContext);
            entity = new NByteArrayEntity(body);
        }

        HttpEntityEnclosingRequestBase request = requestFactory.create();

        request.setURI(uri);

        request.setEntity(entity);


        headerProvider.headers().forEach((headerName, headerValue) -> {
            // If multipart, the content type is set by the underlying client.
            if (HttpHeader.CONTENT_TYPE.equalsIgnoreCase(headerName)) {
                if (multipart == null || !multipart) {
                    request.addHeader(headerName, headerValue); // we only add it when
                }
            } else {
                request.addHeader(headerName, headerValue);
            }
        });

        return client.execute(HttpAsyncMethods.create(request), callback);
    }

    static class HttpEntityWrapper implements HttpEntity {

        private final HttpEntity delegate;

        HttpEntityWrapper(HttpEntity delegate) {
            this.delegate = delegate;
        }

        @Override
        public boolean isRepeatable() {
            return this.delegate.isRepeatable();
        }

        @Override
        public boolean isChunked() {
            return this.delegate.isChunked();
        }

        @Override
        public long getContentLength() {
            return this.delegate.getContentLength();
        }

        @Override
        public Header getContentType() {
            return this.delegate.getContentType();
        }

        @Override
        public Header getContentEncoding() {
            return this.delegate.getContentEncoding();
        }

        @Override
        public InputStream getContent() throws IOException, UnsupportedOperationException {
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            writeTo(outStream);
            outStream.flush();
            return new ByteArrayInputStream(outStream.toByteArray());
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            this.delegate.writeTo(outstream);
        }

        @Override
        public boolean isStreaming() {
            return this.delegate.isStreaming();
        }

        @Override
        public void consumeContent() throws IOException {
            this.delegate.consumeContent();
        }
    }
}
