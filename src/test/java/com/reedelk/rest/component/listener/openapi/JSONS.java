package com.reedelk.rest.component.listener.openapi;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class JSONS {

    public enum InfoObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "info_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "info_object_with_default_properties.json";
            }
        }
    }

    public enum LicenseObject implements Provider {

        WithAllProperties() {
            @Override
            public String path() {
                return "license_object_with_all_properties.json";
            }
        },

        WithDefaultProperties() {
            @Override
            public String path() {
                return "license_object_with_default_properties.json";
            }
        },
    }


    interface Provider {

        String path();

        default URL url() {
            return JSONS.class.getResource(path());
        }

        default String string() {
            try (Scanner scanner = new Scanner(url().openStream(), UTF_8.toString())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            } catch (IOException e) {
                throw new RuntimeException("String from URI could not be read.", e);
            }
        }
    }
}
