package de.codecentric.reedelk.rest.internal.commons;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class URLDecoderUtil {

    public static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            // If we can't decode it, we return the original value.
            // This would only happen if the UTF-8 encoding is not supported by the SDK,
            // which should never happen.
            return value;
        }
    }
}
