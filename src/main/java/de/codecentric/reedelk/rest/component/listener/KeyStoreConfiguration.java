package de.codecentric.reedelk.rest.component.listener;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = KeyStoreConfiguration.class, scope = PROTOTYPE)
public class KeyStoreConfiguration implements Implementor {

    @Property("Path")
    @Hint("/var/certificates/myKeyStore.jks")
    @Example("/var/certificates/myKeyStore.jks")
    @Description("The path on the filesystem of the key store.")
    private String path;

    @Property("Password")
    @Password
    @Example("myKeyStorePassword")
    @Description("The keystore password.")
    private String password;

    @Property("Type")
    @Hint("JKS")
    @Example("JKS")
    @Description("The keystore type.")
    private String type;

    @Property("Algorithm")
    @Hint("SunX509")
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
