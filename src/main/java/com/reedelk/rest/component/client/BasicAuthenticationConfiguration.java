package com.reedelk.rest.component.client;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = BasicAuthenticationConfiguration.class, scope = PROTOTYPE)
public class BasicAuthenticationConfiguration implements Implementor {

    @Property("Username")
    @Hint("myUsername")
    @Example("user001")
    @Description("The username to be used in the remote server basic authentication.")
    private String username;

    @Property("Password")
    @Password
    @Example("password001")
    @Description("The password to be used in the remote server basic authentication.")
    private String password;

    @Property("Preemptive")
    @Example("true")
    @DefaultValue("false")
    @Description("Immediately sends basic authentication header before the server answers with unauthorized response code.")
    private Boolean preemptive;

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

    public Boolean getPreemptive() {
        return preemptive;
    }

    public void setPreemptive(Boolean preemptive) {
        this.preemptive = preemptive;
    }
}
