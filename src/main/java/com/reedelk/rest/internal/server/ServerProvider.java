package com.reedelk.rest.internal.server;

import com.reedelk.rest.component.RESTListenerConfiguration;
import com.reedelk.rest.internal.commons.Defaults;
import com.reedelk.rest.internal.commons.HostNamePortKey;
import com.reedelk.rest.internal.openapi.OpenApiServerDecorator;
import org.osgi.service.component.annotations.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.*;
import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = ServerProvider.class, scope = SINGLETON)
public class ServerProvider {

    private Map<HostNamePortKey, Server> serverMap = new ConcurrentHashMap<>();

    public Optional<Server> getOrCreate(RESTListenerConfiguration configuration) {
        HostNamePortKey key = new HostNamePortKey(
                Defaults.RestListener.host(configuration.getHost()),
                Defaults.RestListener.port(configuration.getPort(), configuration.getProtocol()));
        if (!serverMap.containsKey(key)) {

            boolean openApiDisabled = isOpenApiDisabled(configuration);

            Server server = new DefaultServer(configuration);
            if (!openApiDisabled) {
                // If the open api is NOT disabled we apply the OpenAPI decorator
                // which applies for each route the OpenAPI definition.
                server = new OpenApiServerDecorator(configuration, server);
            }
            serverMap.put(key, server);
        }
        Server server = serverMap.get(key);
        checkBasePathIsConsistent(configuration, server);
        return of(server);
    }

    public Optional<Server> get(RESTListenerConfiguration configuration) {
        if (configuration == null) return empty();
        HostNamePortKey key = new HostNamePortKey(
                Defaults.RestListener.host(configuration.getHost()),
                Defaults.RestListener.port(configuration.getPort(), configuration.getProtocol()));
        return ofNullable(serverMap.get(key));
    }

    public void release(Server server) {
        // We stop  if and only if there are no
        // more routes associated to this server.
        if (server.hasEmptyRoutes()) {
            server.stop();
            serverMap.entrySet()
                    .stream()
                    .filter(key -> key.getValue() == server)
                    .findFirst()
                    .ifPresent(key -> serverMap.remove(key.getKey()));
        }
    }

    /**
     * If a server bound on a given hostname and port exists already, we
     * make sure that the server and the config have the same base path.
     * If they don't, it means that there are multiple configurations
     * defined on the same host and port but with different base paths.
     */
    private void checkBasePathIsConsistent(RESTListenerConfiguration configuration, Server server) {
        if (!Objects.equals(configuration.getBasePath(), server.getBasePath())) {
            throw new IllegalStateException("There are two server configurations " +
                    "on the same host and port with different base paths");
        }
    }

    private boolean isOpenApiDisabled(RESTListenerConfiguration configuration) {
        return ofNullable(configuration.getDisableOpenApi())
                .orElse(false);
    }
}
