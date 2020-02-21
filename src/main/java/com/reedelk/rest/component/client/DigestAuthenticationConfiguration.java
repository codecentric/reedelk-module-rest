package com.reedelk.rest.component.client;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = DigestAuthenticationConfiguration.class, scope = PROTOTYPE)
public class DigestAuthenticationConfiguration implements Implementor {

    @Property("Username")
    @Example("user001")
    @Description("The username to be used in the remote server digest authentication.")
    private String username;

    @Property("Password")
    @Password
    @Example("password001")
    @Description("The password to be used in the remote server digest authentication.")
    private String password;

    @Property("Preemptive")
    @Example("true")
    @DefaultValue("false")
    @Description("Immediately sends digest authentication header before the server answers with unauthorized response code.")
    private Boolean preemptive;

    @Property("Realm")
    @Example("myRealm")
    @When(propertyName = "preemptive", propertyValue = "true")
    @Description("Realm value to be used in the digest authentication.")
    private String realm;

    @Property("Nonce")
    @Example("123")
    @When(propertyName = "preemptive", propertyValue = "true")
    @Description("Nonce value to be used in the digest authentication if known.")
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
