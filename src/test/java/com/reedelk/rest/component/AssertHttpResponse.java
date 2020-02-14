package com.reedelk.rest.component;

import com.reedelk.rest.client.HttpClientResponseException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.message.content.TypedContent;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class AssertHttpResponse {

    static void isSuccessful(RestClient client,
                             Message message,
                             FlowContext context,
                             String expectedBody,
                             MimeType expectedMimeType) {
        SuccessAssertion successAssertion = new SuccessAssertion(client, message, context, expectedBody, expectedMimeType);
        successAssertion.assertThat();
    }

    static void isSuccessful(RestClient component,
                             Message message,
                             FlowContext context) {
        SuccessAssertion successAssertion = new SuccessAssertion(component, message, context);
        successAssertion.assertThat();
    }

    static void isNotSuccessful(RestClient component,
                                Message message,
                                FlowContext context,
                                int status,
                                String reasonPhrase) {
        UnSuccessAssertion unSuccessAssertion = new UnSuccessAssertion(component, message, context, status, reasonPhrase);
        unSuccessAssertion.assertThat();
    }

    static void isNotSuccessful(RestClient component,
                                Message message,
                                FlowContext context,
                                String expectedErrorMessage) {
        UnSuccessAssertion unSuccessAssertion = new UnSuccessAssertion(component, message, context, expectedErrorMessage);
        unSuccessAssertion.assertThat();
    }

    private static void assertContent(Message message, String expectedContent, MimeType expectedMimeType) {
        assertContent(message, expectedContent);
        TypedContent<?,?> typedContent = message.content();
        assertThat(typedContent.mimeType()).isEqualTo(expectedMimeType);
    }

    private static void assertContent(Message message, String expectedContent) {
        TypedContent<?,?> typedContent = message.content();
        Object stringContent = typedContent.data();
        assertThat(stringContent).isEqualTo(expectedContent);
    }

    private static class SuccessAssertion {

        private final Message message;
        private final String expectedBody;
        private final FlowContext context;
        private final RestClient component;
        private final MimeType expectedMimeType;

        private Message response;
        private Throwable exception;

        SuccessAssertion(RestClient component,
                         Message message,
                         FlowContext context,
                         String expectedBody,
                         MimeType expectedMimeType) {
            this.expectedMimeType = expectedMimeType;
            this.expectedBody = expectedBody;
            this.component = component;
            this.context = context;
            this.message = message;
        }

        SuccessAssertion(RestClient component,
                         Message message,
                         FlowContext context) {
            this(component, message, context, null, null);
        }

        void assertThat() {
            try {
                response = component.apply(context, message);
            } catch (Exception e) {
                exception = e;
            }

            if (response != null) {
                if (expectedBody != null) {
                    assertContent(response, expectedBody, expectedMimeType);
                }
            } else {
                fail("Response was not successful, ", exception);
            }
        }
    }

    private static class UnSuccessAssertion {

        private final Message message;
        private final FlowContext context;
        private final RestClient component;
        private final String expectedErrorMessage;
        private final int expectedStatus;
        private final String expectedReasonPhrase;

        private HttpClientResponseException error;

        UnSuccessAssertion(RestClient component,
                         Message message,
                         FlowContext context,
                         String expectedErrorMessage) {
            this.expectedStatus = -1;
            this.expectedReasonPhrase = null;
            this.message = message;
            this.context = context;
            this.component = component;
            this.expectedErrorMessage = expectedErrorMessage;
        }

        UnSuccessAssertion(RestClient component,
                           Message message,
                           FlowContext context,
                           int expectedStatus,
                           String expectedReasonPhrase) {
            this.expectedErrorMessage = null;
            this.message = message;
            this.context = context;
            this.component = component;
            this.expectedStatus = expectedStatus;
            this.expectedReasonPhrase = expectedReasonPhrase;

        }

        void assertThat() {
            try {
                component.apply(context, message);
            } catch (Exception e) {
                error = (HttpClientResponseException) e;
            }

            if (error != null) {
                if (expectedErrorMessage != null) {
                    Assertions.assertThat(error.getMessage()).isEqualTo(expectedErrorMessage);
                }
                if (expectedStatus >= 0) {
                    Assertions.assertThat(error.getStatusCode()).isEqualTo(expectedStatus);
                }
                if (expectedReasonPhrase != null) {
                    Assertions.assertThat(error.getReasonPhrase()).isEqualTo(expectedReasonPhrase);
                }
            } else {
                fail("Response was successful");
            }
        }
    }
}
