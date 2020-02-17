package com.reedelk.rest.client.body;

import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Parts;
import org.reactivestreams.Publisher;

public interface BodyProvider {

    /**
     * Used by normal transfer encoding. Length
     * of the payload is known in advance.
     * @return the byte array to be sent to the remote host.
     */
    BodyResult get(Message message, FlowContext flowContext);

    /**
     * Used by chunked transfer encoding.
     * @return the byte array stream to be sent to the remote host.
     */
    Publisher<byte[]> getAsStream(Message message, FlowContext flowContext);

    /**
     * Checks whether the content is streamable or not.
     * @return true if the content is a stream based content, false otherwise.
     * @param message the input message.
     */
    boolean streamable(Message message);
}
