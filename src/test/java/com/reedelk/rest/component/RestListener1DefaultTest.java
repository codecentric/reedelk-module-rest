package com.reedelk.rest.component;

import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import static com.reedelk.rest.internal.commons.RestMethod.GET;
import static org.apache.http.HttpStatus.SC_OK;

class RestListener1DefaultTest extends RestListener1AbstractTest {

    @Test
    void shouldUseDefaultHostAndPortWhenConfigDoesNotDefineHostAndPort() {
        // Given
        RestListener1Configuration configWithoutHostAndPortDefined = new RestListener1Configuration();

        int defaultPort = 8080;
        String defaultHost = "localhost";

        HttpGet request = new HttpGet("http://" + defaultHost + ":"+ defaultPort);

        RestListener1 listener = listenerWith(GET, configWithoutHostAndPortDefined);
        listener.addEventListener((message, callback) -> callback.onResult(context, message));
        listener.onStart();

        // Expect
        assertStatusCodeIs(request, SC_OK);
    }
}
