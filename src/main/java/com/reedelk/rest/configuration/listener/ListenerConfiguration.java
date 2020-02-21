package com.reedelk.rest.configuration.listener;

import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Shared
@Component(service = ListenerConfiguration.class, scope = PROTOTYPE)
public class ListenerConfiguration implements Implementor {

    @Property("Host")
    @Hint("localhost")
    @Example("localhost")
    @InitValue("localhost")
    @Description("The host this REST listener will be bound to.")
    private String host;

    @Property("Port")
    @Hint("8080")
    @Example("9292")
    @InitValue("8080")
    @Description("The port this REST listener will be bound to.")
    private Integer port;

    @Property("Protocol")
    @Example("HTTPS")
    @InitValue("HTTP")
    @DefaultValue("HTTP")
    @Description("The http protocol to use. Possible values are: <b>HTTP</b>, <b>HTTPS</b>.")
    private HttpProtocol protocol = HttpProtocol.HTTP;

    @Property("Base path")
    @Hint("/api/v1")
    @Example("/api/v1")
    @Description("The base path shared by all the listeners using this configuration.")
    private String basePath;

    @Property("Keep alive")
    @Example("true")
    @InitValue("true")
    @Description("Enables socket keep alive for this listener.")
    private Boolean keepAlive;

    @Property("Compress response")
    @Example("true")
    @DefaultValue("false")
    @Description("If true the response is compressed before sending it to the client.")
    private Boolean compress;

    @Property("Read timeout millis")
    @Example("30000")
    @Description("Sets the socket read timeout for this listener in milliseconds.")
    private Integer readTimeoutMillis;

    @Property("Connection timeout millis")
    @Example("30000")
    @Description("Sets the socket connection timeout for this listener in milliseconds.")
    private Integer connectionTimeoutMillis;

    @Property("Socket backlog")
    @Example("5")
    @Description("The number of pending connections the listener queue will hold.")
    private Integer socketBacklog;

    @Property("Max chunk size")
    @Example("8192")
    @Description("The maximum length of the content of each chunk.")
    private Integer maxChunkSize;

    @Property("Max headers length")
    @Example("8192")
    @Description("The maximum length of all headers.")
    private Integer maxLengthOfAllHeaders;

    @Property("Security configuration")
    @When(propertyName = "protocol", propertyValue = "HTTPS")
    private SecurityConfiguration securityConfiguration;

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

    public HttpProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(HttpProtocol protocol) {
        this.protocol = protocol;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public Integer getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(Integer readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

    public Integer getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public void setConnectionTimeoutMillis(Integer connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public Boolean getCompress() {
        return compress;
    }

    public void setCompress(Boolean compress) {
        this.compress = compress;
    }

    public Integer getSocketBacklog() {
        return socketBacklog;
    }

    public void setSocketBacklog(Integer socketBacklog) {
        this.socketBacklog = socketBacklog;
    }

    public Integer getMaxChunkSize() {
        return maxChunkSize;
    }

    public void setMaxChunkSize(Integer maxChunkSize) {
        this.maxChunkSize = maxChunkSize;
    }

    public Integer getMaxLengthOfAllHeaders() {
        return maxLengthOfAllHeaders;
    }

    public void setMaxLengthOfAllHeaders(Integer maxLengthOfAllHeaders) {
        this.maxLengthOfAllHeaders = maxLengthOfAllHeaders;
    }

    public SecurityConfiguration getSecurityConfiguration() {
        return securityConfiguration;
    }

    public void setSecurityConfiguration(SecurityConfiguration securityConfiguration) {
        this.securityConfiguration = securityConfiguration;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
