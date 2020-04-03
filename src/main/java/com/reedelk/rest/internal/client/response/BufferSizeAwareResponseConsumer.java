package com.reedelk.rest.internal.client.response;

import org.apache.http.ContentTooLongException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.entity.ContentBufferEntity;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.nio.util.SimpleInputBuffer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;

import java.io.IOException;

public class BufferSizeAwareResponseConsumer extends AbstractAsyncResponseConsumer<HttpResponse> {

    private final int responseBufferSize;

    private HttpResponse response;
    private SimpleInputBuffer buf;

    public static HttpAsyncResponseConsumer<HttpResponse> createConsumer(int responseBufferSize) {
        return new BufferSizeAwareResponseConsumer(responseBufferSize);
    }

    private BufferSizeAwareResponseConsumer(int responseBufferSize) {
        super();
        this.responseBufferSize = responseBufferSize;
    }

    @Override
    protected void onResponseReceived(final HttpResponse response) throws IOException {
        this.response = response;
    }

    @Override
    protected void onEntityEnclosed(
            final HttpEntity entity, final ContentType contentType) throws IOException {
        long len = entity.getContentLength();
        if (len > Integer.MAX_VALUE) {
            throw new ContentTooLongException("Entity content is too long: " + len);
        }
        if (len < 0) {
            len = 4096;
        }
        final int initialBufferSize = Math.min((int) len, responseBufferSize);
        this.buf = new SimpleInputBuffer(initialBufferSize, new HeapByteBufferAllocator());
        this.response.setEntity(new ContentBufferEntity(entity, this.buf));
    }

    @Override
    protected void onContentReceived(
            final ContentDecoder decoder, final IOControl ioctrl) throws IOException {
        Asserts.notNull(this.buf, "Content buffer");
        this.buf.consumeContent(decoder);
    }

    @Override
    protected void releaseResources() {
        this.response = null;
        this.buf = null;
    }

    @Override
    protected HttpResponse buildResult(final HttpContext context) {
        return this.response;
    }
}
