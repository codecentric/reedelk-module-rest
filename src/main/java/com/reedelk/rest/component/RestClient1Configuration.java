package com.reedelk.rest.component;

import com.reedelk.rest.internal.commons.HttpProtocol;
import com.reedelk.rest.component.client.*;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Shared
@Component(service = RestClient1Configuration.class, scope = PROTOTYPE)
public class RestClient1Configuration implements Implementor {

    @Property("id")
    @Hidden
    private String id;

    @Property("Protocol")
    @Example("HTTPS")
    @InitValue("HTTP")
    @DefaultValue("HTTP")
    @Description("The http protocol to be used in the request. Possible values are: <b>HTTP</b, <b>HTTPS</b>.")
    private HttpProtocol protocol;

    // Base URL config
    @Property("Host")
    @Hint("localhost")
    @Example("localhost")
    @InitValue("localhost")
    @Description("Sets the remote host the HTTP request will be sent to.")
    private String host;

    @Property("Port")
    @Hint("8484")
    @Example("8484")
    @InitValue("80")
    @Description("Sets the remote port the HTTP request will be sent to.")
    private Integer port;

    @Property("Base path")
    @Hint("/api/v1")
    @Example("/api/v1")
    @Description("Sets the base request path for all listeners using this configuration.")
    private String basePath;

    // Default request config
    @Property("Request timeout (ms)")
    @Hint("10000")
    @Example("10000")
    @DefaultValue("6000")
    @Description("Returns the timeout in milliseconds used when requesting a connection " +
            "from the connection manager. A timeout value of zero is interpreted as an infinite timeout.")
    private Integer requestTimeout;

    @Property("Connect timeout (ms)")
    @Hint("10000")
    @Example("10000")
    @DefaultValue("6000")
    @Description("Determines the timeout in milliseconds until a connection is established.")
    private Integer connectTimeout;

    @Property("Socket timeout (ms)")
    @Hint("120000")
    @Example("120000")
    @DefaultValue("60000")
    @Description("Defines the socket timeout in milliseconds, " +
            "which is the timeout for waiting for data or, put differently, " +
            "a maximum period inactivity between two consecutive data packets.")
    private Integer socketTimeout;

    @Property("Max Pool Connections")
    @Hint("20")
    @Example("30")
    @DefaultValue("10")
    @Description("Max connections to be kept in the connection pool for all the requests to the given host.")
    private Integer maxPoolConnections;

    @Property("Trust Certificates")
    @Example("true")
    @DefaultValue("false")
    @Description("If true, all requests to a host with a self signed certificate are not verified.")
    private Boolean trustCertificates;

    @Property("Follow redirects")
    @Example("true")
    @InitValue("true")
    @Description("If true, HTTP responses with redirects codes 3xx (301, 308) are automatically followed.")
    private Boolean followRedirects;

    @Property("Expect continue")
    @Example("true")
    @Description("If true, sends Expect: 100-continue header in the initial request before sending the body.")
    private Boolean expectContinue;

    @Property("Keep alive")
    @Example("true")
    @InitValue("true")
    @Description("If true keeps the TCP connection open for multiple HTTP requests/responses.")
    private Boolean keepAlive;

    @Property("Authentication")
    @Example("DIGEST")
    @InitValue("NONE")
    @DefaultValue("NONE")
    @Description("Specifies the type of authentication to be performed on the remote server. Possible values are: <b>NONE</b>, <b>BASIC</b>, <b>DIGEST</b>.")
    private Authentication authentication;

    @Property("Basic authentication")
    @When(propertyName = "authentication", propertyValue = "BASIC")
    private BasicAuthenticationConfiguration basicAuthentication;

    @Property("Digest authentication")
    @When(propertyName = "authentication", propertyValue = "DIGEST")
    private DigestAuthenticationConfiguration digestAuthentication;

    @Property("Proxy")
    @Example("PROXY")
    @InitValue("NONE")
    @DefaultValue("NONE")
    @Description("Enables or disable the use of proxy. Possible values are: <b>NONE</b>, <b>PROXY</b>.")
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

    public Boolean getTrustCertificates() {
        return trustCertificates;
    }

    public void setTrustCertificates(Boolean trustCertificates) {
        this.trustCertificates = trustCertificates;
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
