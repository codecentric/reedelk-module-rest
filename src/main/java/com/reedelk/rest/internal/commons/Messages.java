package com.reedelk.rest.internal.commons;

import com.reedelk.runtime.api.commons.FormattedMessage;

public class Messages {

    private Messages() {
    }

    public enum RestClient implements FormattedMessage {

        REQUEST_FAILED("Failed to connect to %s: %s"),
        REQUEST_CANCELLED("Failed to connect to %s: request has been cancelled"),
        REQUEST_URL_ERROR("Could not build request URL with the following parameters host=[%s], port=[%s], basePath=[%s], scheme=[%s]: %s"),
        CONFIG_CLIENT_NULL_ERROR("Expected JSON definition with 'configuration' object property OR 'baseURL' property"),
        DIGEST_AUTH_MISSING("Digest Authentication Configuration must be present in the JSON definition when 'authentication' property is 'DIGEST'"),
        BASIC_AUTH_MISSING("Basic Authentication Configuration must be present in the JSON definition when 'authentication' property is 'BASIC'"),
        PROXY_CONFIG_MISSING("Proxy Configuration must be present in the JSON definition when 'proxy' property is 'PROXY'"),
        MULTIPART_PART_NULL("Part with name %s is null and it will not be added to the HttpEntity"),
        MULTIPART_PART_CONTENT_UNSUPPORTED("Part with Java Content Type %s is not supported and it will not be added to the HttpEntity");

        private String message;

        RestClient(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }

    public enum RestListener implements FormattedMessage {

        LISTENER_CONFIG_MISSING("RESTListener 'configuration' property must not be null in the JSON definition"),
        ERROR_MULTIPART_ATTRIBUTE_VALUE("Error extracting Multipart attribute value for part named '%s'"),
        ERROR_MULTIPART_FILE_UPLOAD_VALUE("Error extracting Multipart file upload value for part named '%s'"),
        ERROR_MULTIPART_NOT_SUPPORTED("Multipart Content-Type is only supported for requests with method POST and HTTP version 1.1"),
        ERROR_BASE_PATH_NOT_CONSISTENT("There are two server configurations on the same host and port with different base paths. Existing server path: [%s], wanted server path: [%s]."),
        ERROR_ROUTE_ALREADY_DEFINED("Route for method [%s] and path [%s] is already defined.");

        private String message;

        RestListener(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }

    public enum OpenApi implements FormattedMessage {

        DEFAULT_SERVER_DESCRIPTION("Default Server");

        private String message;

        OpenApi(String message) {
            this.message = message;
        }

        @Override
        public String template() {
            return message;
        }
    }
}
