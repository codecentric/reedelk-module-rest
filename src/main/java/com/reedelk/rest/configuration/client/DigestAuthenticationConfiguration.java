package com.reedelk.rest.configuration.client;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = DigestAuthenticationConfiguration.class, scope = PROTOTYPE)
public class DigestAuthenticationConfiguration implements Implementor {

    @Example("user001")
    @Property("Username")
    @PropertyDescription("The username to be used in the remote server digest authentication.")
    private String username;

    @Example("password001")
    @Password
    @Property("Password")
    @PropertyDescription("The password to be used in the remote server digest authentication.")
    private String password;

    @Example("true")
    @DefaultValue("false")
    @Property("Preemptive")
    @PropertyDescription("Immediately sends digest authentication header before the server answers with unauthorized response code.")
    private Boolean preemptive;

    @Example("myRealm")
    @When(propertyName = "preemptive", propertyValue = "true")
    @Property("Realm")
    @PropertyDescription("Realm value to be used in the digest authentication.")
    private String realm;

    @Example("123")
    @When(propertyName = "preemptive", propertyValue = "true")
    @Property("Nonce")
    @PropertyDescription("Nonce value to be used in the digest authentication if known.")
    private String nonce;

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

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
