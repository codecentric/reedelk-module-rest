package de.codecentric.reedelk.rest.internal.openapi;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface JsonProvider {

    String path();

    default URL url() {
        return JsonProvider.class.getResource(path());
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
