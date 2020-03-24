package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = TrustStoreConfiguration.class, scope = PROTOTYPE)
public class TrustStoreConfiguration implements Implementor {

    @Property("Path")
    @Hint("/var/certificates/myTrustStore.jks")
    @Example("/var/certificates/myTrustStore.jks")
    @Description("The path on the filesystem of the trust store.")
    private String path;

    @Property("Password")
    @Password
    @Example("myTrustStorePassword")
    @Description("The trust store password.")
    private String password;

    @Property("Type")
    @Hint("JKS")
    @Example("JKS")
    @Description("The trust store type.")
    private String type;

    @Property("Algorithm")
    @Hint("SunX509")
    @Example("SunX509")
    @Description("The trust store algorithm.")
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
