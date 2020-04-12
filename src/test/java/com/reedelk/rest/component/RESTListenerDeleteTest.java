package com.reedelk.rest.component;

import org.apache.http.client.methods.HttpDelete;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.reedelk.rest.internal.commons.RestMethod.DELETE;
import static org.apache.http.HttpStatus.SC_OK;

class RESTListenerDeleteTest extends RESTListenerAbstractTest {

    private HttpDelete deleteRequest;

    @BeforeEach
    void setUp() {
        super.setUp();
        deleteRequest = new HttpDelete("http://" + DEFAULT_HOST + ":"+ DEFAULT_PORT);
    }

    @Test
    void shouldReturn200() {
        // Given
        RESTListener listener = listenerWith(DELETE, defaultConfiguration);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(deleteRequest, SC_OK);
    }
}
