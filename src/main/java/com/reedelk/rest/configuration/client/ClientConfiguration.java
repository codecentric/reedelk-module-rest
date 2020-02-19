package com.reedelk.rest.configuration.client;

import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Shared
@Component(service = ClientConfiguration.class, scope = PROTOTYPE)
public class ClientConfiguration implements Implementor {

    @Hidden
    @Property("id")
    private String id;

    // Base URL config
    @Example("localhost")
    @InitValue("localhost")
    @Property("Host")
    @PropertyDescription("Sets the remote host the HTTP request will be sent to.")
    private String host;

    @Example("8484")
    @InitValue("80")
    @Property("Port")
    @PropertyDescription("Sets the remote port the HTTP request will be sent to.")
    private Integer port;

    @Example("/api/v1")
    @Property("Base path")
    @PropertyDescription("Sets the base request path for all listeners using this configuration.")
    private String basePath;

    @Example("HTTPS")
    @InitValue("HTTP")
    @DefaultValue("HTTP")
    @Property("Protocol")
    @PropertyDescription("The http protocol to be used in the request. Possible values are: <b>HTTP</b, <b>HTTPS</b>.")
    private HttpProtocol protocol;

    // Default request config
    @Example("10000")
    @DefaultValue("6000")
    @Property("Request timeout (ms)")
    @PropertyDescription("Returns the timeout in milliseconds used when requesting a connection " +
            "from the connection manager. A timeout value of zero is interpreted as an infinite timeout.")
    private Integer requestTimeout;

    @Example("10000")
    @DefaultValue("6000")
    @Property("Connect timeout (ms)")
    @PropertyDescription("Determines the timeout in milliseconds until a connection is established.")
    private Integer connectTimeout;

    @Example("120000")
    @DefaultValue("60000")
    @Property("Socket timeout (ms)")
    @PropertyDescription("Defines the socket timeout in milliseconds, " +
            "which is the timeout for waiting for data or, put differently, " +
            "a maximum period inactivity between two consecutive data packets.")
    private Integer socketTimeout;

    @Hint("20")
    @Example("30")
    @DefaultValue("10")
    @Property("Max Pool Connections")
    @PropertyDescription("Max connections to be kept in the connection pool for all the requests to the given host.")
    private Integer maxPoolConnections;

    @InitValue("true")
    @Example("true")
    @Property("Keep alive")
    @PropertyDescription("If true keeps the TCP connection open for multiple HTTP requests/responses.")
    private Boolean keepAlive;

    @InitValue("true")
    @Example("true")
    @Property("Follow redirects")
    @PropertyDescription("If true, HTTP responses with redirects codes 3xx (301, 308) are automatically followed.")
    private Boolean followRedirects;

    @Example("true")
    @Property("Expect continue")
    @PropertyDescription("If true, sends Expect: 100-continue header in the initial request before sending the body.")
    private Boolean expectContinue;

    @Example("true")
    @DefaultValue("false")
    @Property("Ignore self signed certificates")
    @PropertyDescription("If true, all requests to a host with a self signed certificate are not verified.")
    private Boolean allowSelfSigned;

    @Example("DIGEST")
    @DefaultValue("NONE")
    @InitValue("NONE")
    @Property("Authentication")
    @PropertyDescription("Specifies the type of authentication to be performed on the remote server. Possible values are: <b>NONE</b>, <b>BASIC</b>, <b>DIGEST</b>.")
    private Authentication authentication;

    @When(propertyName = "authentication", propertyValue = "BASIC")
    @Property("Basic authentication")
    private BasicAuthenticationConfiguration basicAuthentication;

    @Property("Digest authentication")
    @When(propertyName = "authentication", propertyValue = "DIGEST")
    private DigestAuthenticationConfiguration digestAuthentication;

    @InitValue("NONE")
    @Example("PROXY")
    @DefaultValue("NONE")
    @Property("Proxy")
    @PropertyDescription("Enables or disable the use of proxy. Possible values are: <b>NONE</b>, <b>PROXY</b>.")
    private Proxy proxy;

    @Property("Proxy configuration")
    @When(propertyName = "proxy", propertyValue = "PROXY")
    private ProxyConfiguration proxyConfiguration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(HttpProtocol protocol) {
        this.protocol = protocol;
    }

    public Integer getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getMaxPoolConnections() {
        return maxPoolConnections;
    }

    public void setMaxPoolConnections(Integer maxPoolConnections) {
        this.maxPoolConnections = maxPoolConnections;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Boolean getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public Boolean getExpectContinue() {
        return expectContinue;
    }

    public void setExpectContinue(Boolean expectContinue) {
        this.expectContinue = expectContinue;
    }

    public Boolean getAllowSelfSigned() {
        return allowSelfSigned;
    }

    public void setAllowSelfSigned(Boolean allowSelfSigned) {
        this.allowSelfSigned = allowSelfSigned;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public BasicAuthenticationConfiguration getBasicAuthentication() {
        return basicAuthentication;
    }

    public void setBasicAuthentication(BasicAuthenticationConfiguration basicAuthentication) {
        this.basicAuthentication = basicAuthentication;
    }

    public DigestAuthenticationConfiguration getDigestAuthentication() {
        return digestAuthentication;
    }

    public void setDigestAuthentication(DigestAuthenticationConfiguration digestAuthentication) {
        this.digestAuthentication = digestAuthentication;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public ProxyConfiguration getProxyConfiguration() {
        return proxyConfiguration;
    }

    public void setProxyConfiguration(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }
}
