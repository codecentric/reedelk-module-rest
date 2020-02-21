package com.reedelk.rest.client;

import com.reedelk.rest.component.client.BasicAuthenticationConfiguration;
import com.reedelk.rest.component.client.DigestAuthenticationConfiguration;
import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;

import static java.lang.Boolean.TRUE;

public class HttpClientContextProvider {

    private AuthCache authCache = new BasicAuthCache();

    HttpClientContextProvider(HttpHost authHost,
                           BasicAuthenticationConfiguration basicAuthConfig,
                           DigestAuthenticationConfiguration digestAuthConfig) {
        // Preemptive Basic
        if (basicAuthConfig != null && TRUE.equals(basicAuthConfig.getPreemptive())) {
            authCache.put(authHost, new BasicScheme());
        }

        // Preemptive Digest
        if (digestAuthConfig != null && TRUE.equals(digestAuthConfig.getPreemptive())) {
            // Realm and nonce are mandatory in order to compute
            // the Digest auth header when preemptive is expected.
            DigestScheme digestAuth = new DigestScheme();
            digestAuth.overrideParamter("realm", digestAuthConfig.getRealm());
            digestAuth.overrideParamter("nonce", digestAuthConfig.getNonce());
            authCache.put(authHost, digestAuth);
        }
    }

    public HttpClientContext provide() {
        HttpClientContext context = HttpClientContext.create();
        context.setAuthCache(authCache);
        return context;
    }
}
