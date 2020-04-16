package com.reedelk.rest.component;

import com.reedelk.rest.TestComponent;
import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.rest.internal.server.ServerProvider;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;

import static com.reedelk.rest.utils.TestTag.INTEGRATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@ExtendWith(MockitoExtension.class)
@Tag(INTEGRATION)
abstract class RESTListenerAbstractTest {

    private final long moduleId = 10L;
    final ModuleContext moduleContext = new ModuleContext(moduleId);

    static final String TEST_JSON_BODY = "{\"name\":\"John\"}";
    static final String TEST_TEXT_BODY = "This is a sample text";

    static final int DEFAULT_PORT = 8881;
    static final String DEFAULT_HOST = "localhost";

    private static ServerProvider serverProvider;

    @Mock
    protected FlowContext context;
    @Mock
    protected ScriptEngineService scriptEngine;
    protected Object payload;

    Message inboundMessage;
    RESTListenerConfiguration defaultConfiguration;

    private RESTListener listener;

    @BeforeAll
    static void setUpBeforeAll(){
        serverProvider = new ServerProvider();
    }

    @BeforeEach
    void setUp() {
        listener = new RESTListener();
        setField(listener, "provider", serverProvider);
        setField(listener, "scriptEngine", scriptEngine);

        defaultConfiguration = new RESTListenerConfiguration();
        defaultConfiguration.setHost(DEFAULT_HOST);
        defaultConfiguration.setPort(DEFAULT_PORT);
    }

    @AfterEach
    void tearDown() {
        if (listener != null) {
            listener.onShutdown();
        }
    }

    RESTListener listenerWith(RestMethod method, RESTListenerConfiguration configuration) {
        listener.setConfiguration(configuration);
        listener.setMethod(method);
        return listener;
    }

    void assertContentTypeIs(HttpResponse response, String expectedContentType) {
        ContentType contentType = ContentType.get(response.getEntity());
        String actualContentType = contentType.toString();
        assertThat(actualContentType).isEqualTo(expectedContentType);
    }

    void assertStatusCodeIs(HttpUriRequest request, int statusCode) {
        try {
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            assertStatusCodeIs(response, statusCode);
        } catch (IOException e) {
            Assertions.fail(String.format("Error asserting status code=[%d] for request=[%s]", statusCode, request), e);
        }
    }

    void assertContentIs(HttpUriRequest request, String expected) {
        try {
            String content = makeCall(request);
            assertThat(content).isEqualTo(expected);
        } catch (IOException e) {
            Assertions.fail(String.format("Error asserting content=[%s] for request=[%s]", expected, request), e);
        }
    }

    void assertBodySent(RESTListener listener, HttpUriRequest request, Object expectedContent, MimeType expectedMimeType) throws IOException {
        // Execute request
        makeRequest(listener, request);

        // Assertions
        assertThat(payload).isEqualTo(expectedContent);
        assertThat(inboundMessage.content().mimeType()).isEqualTo(expectedMimeType);
    }

    void makeRequest(RESTListener listener, HttpUriRequest request) throws IOException {
        // Setup event listener and start route
        listener.addEventListener((message, callback) ->
                new Thread(() -> {
                    // We must consume the payload in this Thread. Because the
                    // Thread calling the callback is a NIO Thread, hence we would
                    // not be able to consume the payload because it is a Fast non-blocking Thread.
                    inboundMessage = message;
                    payload = message.payload();
                    callback.onResult(context, MessageBuilder.get(TestComponent.class).empty().build());
                }).start());
        listener.onStart();

        // Execute http request
        HttpClientBuilder.create().build().execute(request);
    }

    String makeCall(HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    private void assertStatusCodeIs(HttpResponse response, int expected) {
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(expected);
    }

    private void setField(RESTListener client, String fieldName, Object object) {
        try {
            Field field = client.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(client, object);
        } catch (NoSuchFieldException e) {
            fail(String.format("Could not find  field '%s'", fieldName));
        } catch (IllegalAccessException e) {
            fail(String.format("Could not access field '%s'", fieldName));
        }
    }
}
