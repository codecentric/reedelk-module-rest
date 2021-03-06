package de.codecentric.reedelk.rest.internal.client.uri;

import de.codecentric.reedelk.runtime.api.commons.StringUtils;

import java.util.Map;

public class EmptyURIPathComponent implements URIPathComponent {
    @Override
    public String expand(Map<String, String> pathParams, Map<String, String> queryParams) {
        return StringUtils.EMPTY;
    }
}
