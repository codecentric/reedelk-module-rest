package de.codecentric.reedelk.rest.component;

import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import org.apache.http.client.methods.HttpHead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        RESTListener listener = listenerWith(RestMethod.HEAD, defaultConfiguration);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(headRequest, SC_OK);
    }
}
