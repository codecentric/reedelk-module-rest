package de.codecentric.reedelk.rest.component;

import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import org.apache.http.client.methods.HttpOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;

class RESTListenerOptionsTest extends RESTListenerAbstractTest {

    private HttpOptions optionsRequest;

    @BeforeEach
    void setUp() {
        super.setUp();
        optionsRequest = new HttpOptions("http://" + DEFAULT_HOST + ":"+ DEFAULT_PORT);
    }

    @Test
    void shouldReturn200() {
        // Given
        RESTListener listener = listenerWith(RestMethod.OPTIONS, defaultConfiguration);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(optionsRequest, SC_OK);
    }
}
