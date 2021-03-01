package de.codecentric.reedelk.rest.internal.commons;

import de.codecentric.reedelk.runtime.api.commons.SerializableUtils;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryParameters {

    private QueryParameters() {
    }

    public static HashMap<String, List<String>> from(String uri) {
        if (StringUtils.isBlank(uri)) return new HashMap<>();

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        Map<String, List<String>> queryParameters = decoder.parameters();
        return SerializableUtils.asSerializableMapWithList(queryParameters);
    }
}
