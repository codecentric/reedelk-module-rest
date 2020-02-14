package com.reedelk.rest.client;

import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.rest.commons.Messages;
import com.reedelk.rest.configuration.client.*;
import com.reedelk.runtime.api.exception.ESBException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
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

import static com.reedelk.runtime.api.commons.ConfigurationPreconditions.requireNotNull;
import static java.lang.Boolean.TRUE;
import static org.osgi.service.component.annotations.ServiceScope.SINGLETON;

@Component(service = HttpClientFactory.class, scope = SINGLETON)
public class HttpClientFactory {

    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 6000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 6000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 60000;

    private static final int DEFAULT_MAX_CONNECTION_PER_ROUTE = 5;
    private static final int DEFAULT_MAX_CONNECTIONS = 30;

    private final PoolingNHttpClientConnectionManager sharedConnectionManager;

    public HttpClientFactory() {
        sharedConnectionManager = newDefaultConnectionPool();
    }

    public HttpClient from(ClientConfiguration config) {
        HttpAsyncClientBuilder builder = HttpAsyncClients.custom();

        HttpClientContext context = HttpClientContext.create();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        // Request config
        RequestConfig requestConfig = createRequestConfig(config);

        // Basic authentication config
        Authentication authentication = config.getAuthentication();
        if (Authentication.BASIC.equals(authentication)) {
            BasicAuthenticationConfiguration basicConfig =
                    requireNotNull(ClientConfiguration.class,
                            config.getBasicAuthentication(),
                            Messages.RestClient.BASIC_AUTH_MISSING.format());
            configureBasicAuth(
                    config.getHost(),
                    config.getPort(),
                    config.getProtocol(),
                    basicConfig,
                    credentialsProvider,
                    context);
        }

        // Digest authentication config
        if (Authentication.DIGEST.equals(authentication)) {
            DigestAuthenticationConfiguration digestConfig =
                    requireNotNull(ClientConfiguration.class,
                            config.getDigestAuthentication(),
                            Messages.RestClient.DIGEST_AUTH_MISSING.format());
            configureDigestAuth(
                    config.getHost(),
                    config.getPort(),
                    config.getProtocol(),
                    digestConfig,
                    credentialsProvider,
                    context);
        }

        // Proxy config
        Proxy proxy = config.getProxy();
        if (Proxy.PROXY.equals(proxy)) {
            ProxyConfiguration proxyConfig =
                    requireNotNull(ClientConfiguration.class,
                            config.getProxyConfiguration(),
                            Messages.RestClient.PROXY_CONFIG_MISSING.format());
            configureProxy(
                    proxyConfig,
                    builder,
                    credentialsProvider,
                    context);
        }

        CloseableHttpAsyncClient client = builder
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        return new HttpClient(client, context);
    }

    public HttpClient from(String baseURL) {
        RequestConfig defaultRequestConfig = newDefaultRequestConfig();
        HttpAsyncClientBuilder.create()
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();
        return new HttpClient(HttpAsyncClients.createDefault());
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
        if (sharedConnectionManager != null) {
            try {
                sharedConnectionManager.shutdown();
            } catch (IOException e) {
                // nothing we can do.
            }
        }
    }

    private void configureBasicAuth(String host, int port, HttpProtocol protocol, BasicAuthenticationConfiguration basicAuthConfig, CredentialsProvider credentialsProvider, HttpClientContext context) {
        HttpHost basicAuthHost = new HttpHost(host, port, protocol.name());
        addCredentialsFor(credentialsProvider, basicAuthHost, basicAuthConfig.getUsername(), basicAuthConfig.getPassword());

        // Preemptive
        if (TRUE.equals(basicAuthConfig.getPreemptive())) {
            AuthCache authCache = new BasicAuthCache();
            authCache.put(basicAuthHost, new BasicScheme());
            context.setAuthCache(authCache);
        }
    }

    private void configureDigestAuth(String host, Integer port, HttpProtocol protocol, DigestAuthenticationConfiguration digestAuthConfig, CredentialsProvider credentialsProvider, HttpClientContext context) {
        HttpHost digestAuthHost = new HttpHost(host, port, protocol.name());
        addCredentialsFor(credentialsProvider, digestAuthHost, digestAuthConfig.getUsername(), digestAuthConfig.getPassword());

        // Preemptive
        if (TRUE.equals(digestAuthConfig.getPreemptive())) {
            AuthCache authCache = new BasicAuthCache();
            DigestScheme digestAuth = new DigestScheme();
            // Realm and nonce are mandatory in order to compute
            // the Digest auth header when preemptive is expected.
            digestAuth.overrideParamter("realm", digestAuthConfig.getRealm());
            digestAuth.overrideParamter("nonce", digestAuthConfig.getNonce());
            authCache.put(digestAuthHost, digestAuth);
            context.setAuthCache(authCache);
        }
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
}
