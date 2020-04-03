package com.reedelk.rest.internal.client;

import com.reedelk.rest.component.RestClient;
import com.reedelk.rest.component.RestClientConfiguration;
import com.reedelk.rest.component.client.*;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.osgi.service.component.annotations.Component;

import java.util.*;

import static com.reedelk.rest.internal.commons.Messages.RestClient.*;
import static com.reedelk.runtime.api.commons.ConfigurationPreconditions.requireNotNull;
import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = HttpClientFactory.class, scope = SINGLETON)
public class HttpClientFactory {

    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 6000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;

    private final Map<String, HttpClient> configIdClientMap = new HashMap<>();
    private final Map<String, List<RestClient>> configIdClients = new HashMap<>();

    public synchronized HttpClient create(RestClient listener, RestClientConfiguration configuration) {
        String configId = configuration.getId();
        if (configIdClientMap.containsKey(configId)) {
            List<RestClient> listeners;
            if (!configIdClients.containsKey(configId)) {
                listeners = new ArrayList<>();
                configIdClients.put(configId, listeners);
            } else {
                listeners = configIdClients.get(configId);
            }
            listeners.add(listener);
            return configIdClientMap.get(configId);
        }

        // We need to create a new client...

        HttpAsyncClientBuilder builder = HttpAsyncClients.custom();

        HttpClientContext context = HttpClientContext.create();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        // Connection Pool
        NHttpClientConnectionManager poolConnectionManager =
                HttpClientConnectionManagerFactory.create(configuration);

        // Request config
        RequestConfig requestConfig = createRequestConfig(configuration);

        HttpHost authHost = new HttpHost(configuration.getHost(), configuration.getPort(), configuration.getProtocol().name());

        // Basic authentication config
        Authentication authentication = configuration.getAuthentication();
        if (Authentication.BASIC.equals(authentication)) {
            BasicAuthenticationConfiguration basicConfig =
                    requireNotNull(RestClientConfiguration.class, configuration.getBasicAuthentication(), BASIC_AUTH_MISSING.format());
            configureBasicAuth(authHost, basicConfig, credentialsProvider, context);
        }

        // Digest authentication config
        if (Authentication.DIGEST.equals(authentication)) {
            DigestAuthenticationConfiguration digestConfig =
                    requireNotNull(RestClientConfiguration.class, configuration.getDigestAuthentication(), DIGEST_AUTH_MISSING.format());
            configureDigestAuth(authHost, digestConfig, credentialsProvider, context);
        }

        // Proxy config
        Proxy proxy = configuration.getProxy();
        if (Proxy.PROXY.equals(proxy)) {
            ProxyConfiguration proxyConfig =
                    requireNotNull(RestClientConfiguration.class, configuration.getProxyConfiguration(), PROXY_CONFIG_MISSING.format());
            configureProxy(proxyConfig, builder, credentialsProvider, context);
        }

        CloseableHttpAsyncClient asyncClient = builder
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolConnectionManager)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        HttpClientContextProvider contextProvider = new HttpClientContextProvider(authHost, configuration.getBasicAuthentication(), configuration.getDigestAuthentication());
        HttpClient client = new HttpClient(asyncClient, contextProvider);

        List<RestClient> listeners;
        if (!configIdClients.containsKey(configId)) {
            listeners = new ArrayList<>();
            configIdClients.put(configId, listeners);
        } else {
            listeners = configIdClients.get(configId);
        }
        listeners.add(listener);
        configIdClientMap.put(configId, client);

        client.start();
        return client;
    }

    public synchronized HttpClient create() {
        RequestConfig defaultRequestConfig = newDefaultRequestConfig();
        CloseableHttpAsyncClient client = HttpAsyncClientBuilder.create()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        HttpClient wrapper = new HttpClient(client);
        wrapper.start();
        return wrapper;
    }

    public synchronized void shutdown() {
        // Nothing to do.
        configIdClientMap.forEach((configId, httpClient) -> {
            if (httpClient != null) {
                httpClient.close();
            }
        });
        configIdClientMap.clear();
        configIdClients.clear();
    }

    public synchronized void release(RestClientConfiguration connectionConfig, RestClient client, HttpClient httpClient) {
        if (connectionConfig == null) {
            httpClient.close();
        } else {
            String configId = connectionConfig.getId();
            if (configIdClients.containsKey(configId)) {
                List<RestClient> clients = configIdClients.get(configId);
                clients.remove(client);
                if (clients.isEmpty()) {
                    if (configIdClientMap.containsKey(configId)) {
                        // There are no more users of the client.
                        HttpClient removed = configIdClientMap.remove(configId);
                        if (removed != null) {
                            removed.close();
                        }
                    }
                }
            }
        }
    }

    private void configureBasicAuth(HttpHost host, BasicAuthenticationConfiguration basicAuthConfig, CredentialsProvider credentialsProvider, HttpClientContext context) {
        addCredentialsFor(credentialsProvider, host, basicAuthConfig.getUsername(), basicAuthConfig.getPassword());
    }

    private void configureDigestAuth(HttpHost host, DigestAuthenticationConfiguration digestAuthConfig, CredentialsProvider credentialsProvider, HttpClientContext context) {
        addCredentialsFor(credentialsProvider, host, digestAuthConfig.getUsername(), digestAuthConfig.getPassword());
    }

    private void configureProxy(ProxyConfiguration proxyConfig, HttpAsyncClientBuilder builder, CredentialsProvider credentialsProvider, HttpClientContext context) {
        HttpHost proxyHost = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
        builder.setProxy(proxyHost);

        if (ProxyAuthentication.BASIC.equals(proxyConfig.getAuthentication())) {
            ProxyBasicAuthenticationConfiguration basicAuthConfig = proxyConfig.getBasicAuthentication();
            addCredentialsFor(credentialsProvider, proxyHost, basicAuthConfig.getUsername(), basicAuthConfig.getPassword());

        } else if (ProxyAuthentication.DIGEST.equals(proxyConfig.getAuthentication())) {
            ProxyDigestAuthenticationConfiguration digestAuthConfig = proxyConfig.getDigestAuthentication();
            addCredentialsFor(credentialsProvider, proxyHost, digestAuthConfig.getUsername(), digestAuthConfig.getPassword());
        }
    }

    private RequestConfig createRequestConfig(RestClientConfiguration configuration) {
        RequestConfig.Builder builder = RequestConfig.custom();

        Optional.ofNullable(configuration.getFollowRedirects())
                .ifPresent(isFollowRedirects -> {
                    builder.setRedirectsEnabled(isFollowRedirects);
                    builder.setCircularRedirectsAllowed(isFollowRedirects);
                    builder.setRelativeRedirectsAllowed(isFollowRedirects);
                });

        Optional.ofNullable(configuration.getExpectContinue())
                .ifPresent(builder::setExpectContinueEnabled);

        Integer connectionRequestTimeout = Optional.ofNullable(configuration.getRequestTimeout())
                .orElse(DEFAULT_CONNECTION_REQUEST_TIMEOUT);
        builder.setConnectionRequestTimeout(connectionRequestTimeout);

        Integer connectTimeout = Optional.ofNullable(configuration.getConnectTimeout())
                .orElse(DEFAULT_CONNECT_TIMEOUT);
        builder.setConnectTimeout(connectTimeout);

        Integer socketTimeout = Optional.ofNullable(configuration.getSocketTimeout())
                .orElse(DEFAULT_SOCKET_TIMEOUT);
        builder.setSocketTimeout(socketTimeout);

        return builder.build();
    }

    // See Request Config for documentation on how to set the values correctly.
    private RequestConfig newDefaultRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
    }

    private static void addCredentialsFor(CredentialsProvider provider, HttpHost host, String userName, String password) {
        AuthScope authScope = new AuthScope(host.getHostName(), host.getPort());
        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(userName, password);
        provider.setCredentials(authScope, usernamePasswordCredentials);
    }
}
