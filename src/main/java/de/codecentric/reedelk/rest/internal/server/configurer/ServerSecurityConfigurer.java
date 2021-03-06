package de.codecentric.reedelk.rest.internal.server.configurer;

import de.codecentric.reedelk.rest.component.RESTListenerConfiguration;
import de.codecentric.reedelk.rest.component.listener.*;
import de.codecentric.reedelk.rest.internal.commons.HttpProtocol;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.api.exception.PlatformException;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.tcp.TcpServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Optional;

import static de.codecentric.reedelk.runtime.api.commons.ComponentPrecondition.Configuration.requireNotBlank;
import static java.util.Objects.requireNonNull;

public class ServerSecurityConfigurer {

    public static TcpServer configure(Class<? extends Implementor> configurationClazz, TcpServer bootstrap, RESTListenerConfiguration configuration) {
        // Security is configured if and only if the protocol is HTTPS
        if (!HttpProtocol.HTTPS.equals(configuration.getProtocol())) return bootstrap;
        if (configuration.getSecurityConfiguration() == null) return bootstrap;

        SecurityConfiguration securityConfig = configuration.getSecurityConfiguration();

        return bootstrap.secure(sslContextSpec -> {

            SslContextBuilder contextBuilder;

            ServerSecurityType configurationType = securityConfig.getType();
            if (ServerSecurityType.KEY_STORE.equals(configurationType)) {
                KeyStoreConfiguration keyStoreConfig = requireNonNull(securityConfig.getKeyStore(), "key store config");
                contextBuilder = SslContextBuilder.forServer(getKeyManagerFactory(configurationClazz, keyStoreConfig));

            } else if (ServerSecurityType.CERTIFICATE_AND_PRIVATE_KEY.equals(configurationType)) {
                // TODO: Error handling
                CertificateAndPrivateKeyConfiguration config =
                        requireNonNull(securityConfig.getCertificateAndPrivateKey(), "certificate and private key configuration");
                File certificateFile = new File(config.getCertificateFile());
                File privateKeyFile = new File(config.getPrivateKeyFile());
                contextBuilder = SslContextBuilder.forServer(certificateFile, privateKeyFile);

            } else {
                throw new PlatformException("Wrong config");
            }

            if (securityConfig.getUseTrustStore() != null && securityConfig.getUseTrustStore()) {
                TrustStoreConfiguration trustStoreConfiguration =
                        requireNonNull(securityConfig.getTrustStoreConfiguration(), "trust store config");
                contextBuilder.trustManager(getTrustManagerFactory(configurationClazz, trustStoreConfiguration));
            }

            try {
                sslContextSpec.sslContext(contextBuilder.build());
            } catch (SSLException e) {
                throw new PlatformException(e);
            }
        });
    }


    private static TrustManagerFactory getTrustManagerFactory(Class<? extends Implementor> configurationClazz, TrustStoreConfiguration config) {
        String type = config.getType();
        String algorithm = config.getAlgorithm();
        String location = requireNotBlank(configurationClazz, config.getPath(), "Trust store location must not be empty");
        String password = requireNotBlank(configurationClazz, config.getPassword(), "Trust store password must not be empty");
        try {
            String alg = Optional.ofNullable(algorithm).orElse(TrustManagerFactory.getDefaultAlgorithm());
            TrustManagerFactory factory = TrustManagerFactory.getInstance(alg);
            KeyStore keyStore = type == null ? KeyStore.getInstance(KeyStore.getDefaultType()) : KeyStore.getInstance(type);
            try (FileInputStream fileInputStream = new FileInputStream(location)) {
                keyStore.load(fileInputStream, password.toCharArray());
            }
            factory.init(keyStore);
            return factory;
        } catch (Exception exception) {
            throw new PlatformException(exception);
        }
    }

    private static KeyManagerFactory getKeyManagerFactory(Class<? extends Implementor> configurationClazz, KeyStoreConfiguration config) {
        String type = config.getType();
        String algorithm = config.getAlgorithm();
        String location = requireNotBlank(configurationClazz, config.getPath(), "Key store location must not be empty");
        String password = requireNotBlank(configurationClazz, config.getPassword(), "Key store password must not be empty");
        try {
            String alg = Optional.ofNullable(algorithm).orElse(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = type == null ? KeyStore.getInstance(KeyStore.getDefaultType()) : KeyStore.getInstance(type);
            KeyManagerFactory factory = KeyManagerFactory.getInstance(alg);
            try (FileInputStream fileInputStream = new FileInputStream(location)) {
                keyStore.load(fileInputStream, password.toCharArray());
            }
            factory.init(keyStore, password.toCharArray());
            return factory;
        } catch (Exception exception) {
            throw new PlatformException(exception);
        }
    }
}
