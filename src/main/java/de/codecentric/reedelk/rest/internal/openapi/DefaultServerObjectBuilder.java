package de.codecentric.reedelk.rest.internal.openapi;

import de.codecentric.reedelk.openapi.v3.model.ServerObject;
import de.codecentric.reedelk.rest.component.RESTListenerConfiguration;
import de.codecentric.reedelk.rest.internal.commons.Defaults;
import de.codecentric.reedelk.rest.internal.commons.HttpProtocol;
import de.codecentric.reedelk.rest.internal.commons.Messages;
import de.codecentric.reedelk.runtime.api.commons.StringUtils;

public class DefaultServerObjectBuilder {

    private DefaultServerObjectBuilder() {
    }

    public static ServerObject from(RESTListenerConfiguration configuration) {
        String host = Defaults.RestListener.host(configuration.getHost());
        int port = Defaults.RestListener.port(configuration.getPort(), configuration.getProtocol());

        String basePath = configuration.getBasePath();
        basePath = StringUtils.isBlank(basePath) ? StringUtils.EMPTY : basePath;

        HttpProtocol protocol = configuration.getProtocol();
        ServerObject defaultServerObject = new ServerObject();
        defaultServerObject.setUrl(protocol.name().toLowerCase() + "://" + host + ":" + port + basePath);
        defaultServerObject.setDescription(Messages.OpenApi.DEFAULT_SERVER_DESCRIPTION.format());
        return defaultServerObject;
    }
}
