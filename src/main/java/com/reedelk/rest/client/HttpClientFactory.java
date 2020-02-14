package com.reedelk.rest.client;

import com.reedelk.rest.configuration.client.*;
import com.reedelk.runtime.api.exception.ESBException;
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
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.util.Optional;

import static com.reedelk.rest.commons.Messages.RestClient.*;
import static com.reedelk.runtime.api.commons.ConfigurationPreconditions.requireNotNull;
import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = HttpClientFactory.class, scope = SINGLETON)
public class HttpClientFactory {


    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 6000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;

    private static final int DEFAULT_MAX_CONNECTION_PER_ROUTE = 5;
    private static final int DEFAULT_MAX_CONNECTIONS = 30;

    private final PoolingNHttpClientConnectionManager connectionManager;

    public HttpClientFactory() {
        connectionManager = newDefaultConnectionPool();
    }

    public HttpClient create(ClientConfiguration config) {
        HttpAsyncClientBuilder builder = HttpAsyncClients.custom();

        HttpClientContext context = HttpClientContext.create();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        // Connection Pool
        PoolingNHttpClientConnectionManager poolConnectionManager = createConnectionManager(config);

        // Request config
        RequestConfig requestConfig = createRequestConfig(config);

        HttpHost authHost = new HttpHost(config.getHost(), config.getPort(), config.getProtocol().name());

        // Basic authentication config
        Authentication authentication = config.getAuthentication();
        if (Authentication.BASIC.equals(authentication)) {
            BasicAuthenticationConfiguration basicConfig =
                    requireNotNull(ClientConfiguration.class, config.getBasicAuthentication(), BASIC_AUTH_MISSING.format());
            configureBasicAuth(authHost, basicConfig, credentialsProvider, context);
        }

        // Digest authentication config
        if (Authentication.DIGEST.equals(authentication)) {
            DigestAuthenticationConfiguration digestConfig =
                    requireNotNull(ClientConfiguration.class, config.getDigestAuthentication(), DIGEST_AUTH_MISSING.format());
            configureDigestAuth(authHost, digestConfig, credentialsProvider, context);
        }

        // Proxy config
        Proxy proxy = config.getProxy();
        if (Proxy.PROXY.equals(proxy)) {
            ProxyConfiguration proxyConfig =
                    requireNotNull(ClientConfiguration.class, config.getProxyConfiguration(), PROXY_CONFIG_MISSING.format());
            configureProxy(proxyConfig, builder, credentialsProvider, context);
        }

        CloseableHttpAsyncClient client = builder
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolConnectionManager)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        HttpClientContextProvider contextProvider = new HttpClientContextProvider(authHost, config.getBasicAuthentication(), config.getDigestAuthentication());
        return new HttpClient(client, contextProvider);
    }

    public HttpClient create() {
        RequestConfig defaultRequestConfig = newDefaultRequestConfig();
        CloseableHttpAsyncClient client = HttpAsyncClientBuilder.create()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(connectionManager)
                // The HttpClientFactory manages the connection manager,
                // therefore dont want it to be automatically closed when the client is closed.
                .setConnectionManagerShared(true)
                .build();
        return new HttpClient(client);
    }

    private RequestConfig newDefaultRequestConfig() {
        // See Request Config for documentation on how to set the values correctly.
        return RequestConfig.custom()
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                .build();
    }

    public void shutdown() {
        if (connectionManager != null) {
            try {
                connectionManager.shutdown();
            } catch (IOException e) {
                // nothing we can do.
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

    private RequestConfig createRequestConfig(ClientConfiguration configuration) {
        RequestConfig.Builder builder = RequestConfig.custom();

        Optional.ofNullable(configuration.getFollowRedirects())
                .ifPresent(isFollowRedirects -> {
                    builder.setRedirectsEnabled(isFollowRedirects);
                    builder.setCircularRedirectsAllowed(isFollowRedirects);
                    builder.setRelativeRedirectsAllowed(isFollowRedirects);
                });

        Optional.ofNullable(configuration.getExpectContinue())
                .ifPresent(builder::setExpectContinueEnabled);

        Optional.ofNullable(configuration.getRequestTimeout())
                .ifPresent(builder::setConnectionRequestTimeout);

        Optional.ofNullable(configuration.getConnectTimeout())
                .ifPresent(builder::setConnectTimeout); // or Else ..default

        Optional.ofNullable(configuration.getSocketTimeout())
                .ifPresent(builder::setSocketTimeout); // or Else ... default

        return builder.build();
    }

    private static void addCredentialsFor(CredentialsProvider provider, HttpHost host, String userName, String password) {
        provider.setCredentials(
                new AuthScope(host.getHostName(), host.getPort()),
                new UsernamePasswordCredentials(userName, password));
    }

    private PoolingNHttpClientConnectionManager newDefaultConnectionPool() {
        DefaultConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor();
        } catch (IOReactorException e) {
            throw new ESBException(e);
        }
        PoolingNHttpClientConnectionManager pool = new PoolingNHttpClientConnectionManager(ioReactor);
        pool.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTION_PER_ROUTE);
        pool.setMaxTotal(DEFAULT_MAX_CONNECTIONS);
        return pool;
    }

    private static final int DEFAULT_CONNECTIONS_CLIENT = 10;

    private PoolingNHttpClientConnectionManager createConnectionManager(ClientConfiguration configuration) {
        DefaultConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor();
        } catch (IOReactorException e) {
            throw new ESBException(e);
        }
        int maxConnections = Optional.ofNullable(configuration.getMaxPoolConnections()).orElse(DEFAULT_CONNECTIONS_CLIENT);
        PoolingNHttpClientConnectionManager pool = new PoolingNHttpClientConnectionManager(ioReactor);
        pool.setDefaultMaxPerRoute(maxConnections);
        pool.setMaxTotal(maxConnections);
        return pool;
    }
}
