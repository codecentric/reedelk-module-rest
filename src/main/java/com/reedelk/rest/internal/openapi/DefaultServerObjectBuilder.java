package com.reedelk.rest.internal.openapi;

import com.reedelk.openapi.v3.model.ServerObject;
import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.internal.commons.Defaults;
import com.reedelk.rest.internal.commons.HttpProtocol;
import com.reedelk.rest.internal.commons.Messages;
import com.reedelk.runtime.api.commons.StringUtils;

public class DefaultServerObjectBuilder {

    // TODO: Testme
    public static ServerObject from(RESTListenerConfiguration configuration) {
        String host = Defaults.RestListener.host(configuration.getHost());
        int port = Defaults.RestListener.port(configuration.getPort(), configuration.getProtocol());

        String basePath = configuration.getBasePath();
        basePath = StringUtils.isBlank(basePath) ? Defaults.RestListener.path() : basePath;

        HttpProtocol protocol = configuration.getProtocol();
        ServerObject defaultServerObject = new ServerObject();
        defaultServerObject.setUrl(protocol.name().toLowerCase() + "://" + host + ":" + port + basePath);
        defaultServerObject.setDescription(Messages.OpenApi.DEFAULT_SERVER_DESCRIPTION.format());
        return defaultServerObject;
    }
}
