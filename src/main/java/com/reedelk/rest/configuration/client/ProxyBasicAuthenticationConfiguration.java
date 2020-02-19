package com.reedelk.rest.configuration.client;

import com.reedelk.runtime.api.annotation.Example;
import com.reedelk.runtime.api.annotation.Password;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.PropertyDescription;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = ProxyBasicAuthenticationConfiguration.class, scope = PROTOTYPE)
public class ProxyBasicAuthenticationConfiguration implements Implementor {

    @Example("myProxyUsername")
    @Property("Username")
    @PropertyDescription("The username to be used in the basic proxy authentication.")
    private String username;

    @Example("myProxyPassword")
    @Password
    @Property("Password")
    @PropertyDescription("The password to be used in the basic proxy authentication.")
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
