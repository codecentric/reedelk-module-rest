package de.codecentric.reedelk.rest.internal.client.strategy;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.*;

/**
 * This wrapper avoids exception thrown when the Multipart content is greater than 25 * 1024,
 * thrown by the wrapped org.apache.http.entity.mime.MultipartFormEntity.
 */
public class MultipartFormEntityWrapper implements HttpEntity {

    private final HttpEntity delegate;

    MultipartFormEntityWrapper(HttpEntity delegate) {
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
        delegate.consumeContent();
    }
}
