package de.codecentric.reedelk.rest.component.client;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = ProxyBasicAuthenticationConfiguration.class, scope = PROTOTYPE)
public class ProxyBasicAuthenticationConfiguration implements Implementor {

    @Property("Username")
    @Hint("myProxyUsername")
    @Example("myProxyUsername")
    @Description("The username to be used in the basic proxy authentication.")
    private String username;

    @Property("Password")
    @Password
    @Example("myProxyPassword")
    @Description("The password to be used in the basic proxy authentication.")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
