package com.reedelk.rest.internal.openapi;

public enum Examples implements JsonProvider {

    OpenApiEmpty() {
        @Override
        public String path() {
            return "open_api_empty.json";
        }
    },

    OpenApiCustomBasePath() {
        @Override
        public String path() {
            return "open_api_with_custom_base_path.json";
        }
    },

    OpenApiWithPathAndMethod() {
        @Override
        public String path() {
            return "open_api_with_path_and_method.json";
        }
    },

    OpenApiWithPathAndMethodAndHeaders() {
        @Override
        public String path() {
            return "open_api_with_path_and_method_and_headers.json";
        }
    },

    OpenApiWithPathAndMethodAndHeadersError() {
        @Override
        public String path() {
            return "open_api_with_path_and_method_and_headers_error.json";
        }
    },

    OpenApiWithPathParams() {
        @Override
        public String path() {
            return "open_api_with_path_params.json";
        }
    },

    OpenApiWithOverriddenPathParams() {
        @Override
        public String path() {
            return "open_api_with_overridden_path_params.json";
        }
    },

    OpenApiWithSchemas() {
        @Override
        public String path() {
            return "open_api_with_schemas.json";
        }
    }
}
