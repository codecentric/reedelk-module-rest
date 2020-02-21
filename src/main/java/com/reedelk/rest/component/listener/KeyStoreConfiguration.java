package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.Description;
import com.reedelk.runtime.api.annotation.Example;
import com.reedelk.runtime.api.annotation.Password;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = KeyStoreConfiguration.class, scope = PROTOTYPE)
public class KeyStoreConfiguration implements Implementor {

    @Property("Path")
    @Example("/var/certificates/myKeyStore.jks")
    @Description("The path on the filesystem of the key store.")
    private String path;

    @Property("Password")
    @Password
    @Example("myKeyStorePassword")
    @Description("The keystore password.")
    private String password;

    @Property("Type")
    @Example("JKS")
    @Description("The keystore type.")
    private String type;

    @Property("Algorithm")
    @Example("SunX509")
    @Description("The keystore algorithm.")
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
