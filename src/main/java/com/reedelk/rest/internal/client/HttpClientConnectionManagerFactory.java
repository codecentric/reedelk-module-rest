package com.reedelk.rest.internal.client;

import com.reedelk.rest.component.RestClient1Configuration;
import com.reedelk.runtime.api.exception.ESBException;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class HttpClientConnectionManagerFactory {

    private static final int DEFAULT_CONNECTIONS_CLIENT = 10;

    private HttpClientConnectionManagerFactory() {
    }

    public static NHttpClientConnectionManager create(RestClient1Configuration configuration) {
            boolean isTrustCertificates = Optional.ofNullable(configuration.getTrustCertificates()).orElse(false);
            Registry<SchemeIOSessionStrategy> registry = createRegistry(isTrustCertificates);
            int maxConnections = Optional.ofNullable(configuration.getMaxPoolConnections()).orElse(DEFAULT_CONNECTIONS_CLIENT);

        try {
            DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager pool = new PoolingNHttpClientConnectionManager(ioReactor, registry);
            pool.setDefaultMaxPerRoute(maxConnections);
            pool.setMaxTotal(maxConnections);
            return pool;
        } catch (Exception exception) {
            throw new ESBException(exception);
        }
    }

    // Allow all certificates: don't verify hostname and always trust any certificate.
    private static Registry<SchemeIOSessionStrategy> createRegistry(boolean isTrustCertificates) {
        try {
            if (isTrustCertificates) {
                SSLContextBuilder sslContextBuilder = SSLContexts.custom();
                sslContextBuilder.loadTrustMaterial(null, TRUST_ALWAYS_STRATEGY);
                return RegistryBuilder.<SchemeIOSessionStrategy>create()
                        .register("http", NoopIOSessionStrategy.INSTANCE)
                        .register("https", new SSLIOSessionStrategy(sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE))
                        .build();
            } else {
                return RegistryBuilder.<SchemeIOSessionStrategy>create()
                        .register("http", NoopIOSessionStrategy.INSTANCE)
                        .register("https", SSLIOSessionStrategy.getDefaultStrategy())
                        .build();
            }
        } catch (Exception exception) {
            throw new ESBException(exception);
        }
    }

    private static final TrustStrategy TRUST_ALWAYS_STRATEGY = new AlwaysTrustedStrategy();

    static class AlwaysTrustedStrategy implements TrustStrategy {
        @Override
        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return true;
        }
    }
}
