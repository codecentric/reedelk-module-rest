package com.reedelk.rest.configuration.listener;

import com.reedelk.runtime.api.annotation.Example;
import com.reedelk.runtime.api.annotation.Password;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.PropertyDescription;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = KeyStoreConfiguration.class, scope = PROTOTYPE)
public class KeyStoreConfiguration implements Implementor {

    @Example("/var/certificates/myKeyStore.jks")
    @Property("Path")
    @PropertyDescription("The path on the filesystem of the key store.")
    private String path;

    @Example("myKeyStorePassword")
    @Password
    @Property("Password")
    @PropertyDescription("The keystore password.")
    private String password;

    @Example("JKS")
    @Property("Type")
    @PropertyDescription("The keystore type.")
    private String type;

    @Example("SunX509")
    @Property("Algorithm")
    @PropertyDescription("The keystore algorithm.")
    private String algorithm;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
