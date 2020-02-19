package com.reedelk.rest.configuration.client;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = ProxyConfiguration.class, scope = PROTOTYPE)
public class ProxyConfiguration implements Implementor {

    @Example("http://myproxy.com")
    @Property("Host")
    @PropertyDescription("The proxy host name.")
    private String host;

    @Example("8686")
    @InitValue("8080")
    @Property("Port")
    @PropertyDescription("The proxy port.")
    private Integer port;

    @Example("DIGEST")
    @InitValue("NONE")
    @DefaultRenameMe("NONE")
    @Property("Authentication")
    @PropertyDescription("The proxy authentication scheme to use. Possible values are: <b>NONE</b>, <b>BASIC</b>, <b>DIGEST</b>.")
    private ProxyAuthentication authentication;

    @Property("Basic authentication")
    @When(propertyName = "authentication", propertyValue = "BASIC")
    private ProxyBasicAuthenticationConfiguration basicAuthentication;

    @Property("Digest authentication")
    @When(propertyName = "authentication", propertyValue = "DIGEST")
    private ProxyDigestAuthenticationConfiguration digestAuthentication;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ProxyAuthentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(ProxyAuthentication authentication) {
        this.authentication = authentication;
    }

    public ProxyBasicAuthenticationConfiguration getBasicAuthentication() {
        return basicAuthentication;
    }

    public void setBasicAuthentication(ProxyBasicAuthenticationConfiguration basicAuthentication) {
        this.basicAuthentication = basicAuthentication;
    }

    public ProxyDigestAuthenticationConfiguration getDigestAuthentication() {
        return digestAuthentication;
    }

    public void setDigestAuthentication(ProxyDigestAuthenticationConfiguration digestAuthentication) {
        this.digestAuthentication = digestAuthentication;
    }
}
