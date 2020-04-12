package com.reedelk.rest.component;

import org.apache.http.client.methods.HttpHead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.reedelk.rest.internal.commons.RestMethod.HEAD;
import static org.apache.http.HttpStatus.SC_OK;

class RESTListenerHeadTest extends RESTListenerAbstractTest {

    private HttpHead headRequest;

    @BeforeEach
    void setUp() {
        super.setUp();
        headRequest = new HttpHead("http://" + DEFAULT_HOST + ":"+ DEFAULT_PORT);
    }

    @Test
    void shouldReturn200() {
        // Given
        RESTListener listener = listenerWith(HEAD, defaultConfiguration);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(headRequest, SC_OK);
    }
}
