package de.codecentric.reedelk.rest.component;

import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static org.apache.http.HttpStatus.SC_OK;

class RESTListenerDefaultTest extends RESTListenerAbstractTest {

    @Test
    void shouldUseDefaultHostAndPortWhenConfigDoesNotDefineHostAndPort() {
        // Given
        RESTListenerConfiguration configWithoutHostAndPortDefined = new RESTListenerConfiguration();

        int defaultPort = 8080;
        String defaultHost = "localhost";

        HttpGet request = new HttpGet("http://" + defaultHost + ":"+ defaultPort);

        RESTListener listener = listenerWith(RestMethod.GET, configWithoutHostAndPortDefined);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(request, SC_OK);
    }
}
