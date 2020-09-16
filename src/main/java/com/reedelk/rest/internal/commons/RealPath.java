package com.reedelk.rest.internal.commons;

import com.reedelk.rest.component.RESTListenerConfiguration;

import static com.reedelk.runtime.api.commons.StringUtils.*;

public class RealPath {

    /**
     * Returns the real path, with the base path prefixed to the given
     * path if it is not blank.
     *
     * @param path the original path.
     * @return the base path + original path if the base path is not blank,
     * otherwise the original path is returned.
     */
    public static String from(RESTListenerConfiguration configuration, String path) {
        String thePath = isBlank(path) ? EMPTY : path;
        String basePath = configuration.getBasePath();
        if (isNotBlank(basePath) && Defaults.RestListener.path().equals(basePath)) {
            return isBlank(thePath) ? Defaults.RestListener.path() : thePath;
        } else if (isNotBlank(basePath)) {
            return basePath + thePath;
        } else if (isBlank(thePath)) {
            return Defaults.RestListener.path();
        } else {
            return thePath;
        }
    }
}
