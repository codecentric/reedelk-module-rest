package com.reedelk.rest.component;

import org.apache.http.client.methods.HttpHead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.reedelk.rest.internal.commons.RestMethod.HEAD;
import static org.apache.http.HttpStatus.SC_OK;

class RestListener1HeadTest extends RestListener1AbstractTest {

    private HttpHead headRequest;

    @BeforeEach
    void setUp() {
        super.setUp();
        headRequest = new HttpHead("http://" + DEFAULT_HOST + ":"+ DEFAULT_PORT);
    }

    @Test
    void shouldReturn200() {
        // Given
        RestListener1 listener = listenerWith(HEAD, defaultConfiguration);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(headRequest, SC_OK);
    }
}
