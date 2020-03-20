package com.reedelk.rest.openapi;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public enum JSONS {

    EmptyOpenAPI {
        @Override
        public String path() {
            return "/com/reedelk/rest/openapi/empty.openapi.json";
        }
    },

    MultipleResponsesForPath {
        @Override
        public String path() {
            return "/com/reedelk/rest/openapi/multiple_responses_statuses_for_path_and_method.openapi.json";
        }
    },

    SampleOpenAPI {
        @Override
        public String path() {
            return "/com/reedelk/rest/openapi/sample.openapi.json";
        }
    };

    public URL url() {
        return JSONS.class.getResource(path());
    }

    public abstract String path();

    public String string() {
        try (Scanner scanner = new Scanner(url().openStream(), UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            throw new RuntimeException("String from URI could not be read.", e);
        }
    }
}
