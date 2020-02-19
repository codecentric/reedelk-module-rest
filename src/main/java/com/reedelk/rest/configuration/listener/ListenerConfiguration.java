package com.reedelk.rest.configuration.listener;

import com.reedelk.rest.commons.HttpProtocol;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Shared
@Component(service = ListenerConfiguration.class, scope = PROTOTYPE)
public class ListenerConfiguration implements Implementor {

    @Hint("localhost")
    @Example("localhost")
    @InitValue("localhost")
    @Property("Host")
    @PropertyDescription("The host this REST listener will be bound to.")
    private String host;

    @Hint("8080")
    @Example("9292")
    @InitValue("8080")
    @Property("Port")
    @PropertyDescription("The port this REST listener will be bound to.")
    private Integer port;

    @Example("HTTPS")
    @InitValue("HTTP")
    @DefaultValue("HTTP")
    @Property("Protocol")
    @PropertyDescription("The http protocol to use. Possible values are: <b>HTTP</b>, <b>HTTPS</b>.")
    private HttpProtocol protocol = HttpProtocol.HTTP;

    @Hint("/api/v1")
    @Example("/api/v1")
    @Property("Base path")
    @PropertyDescription("The base path shared by all the listeners using this configuration.")
    private String basePath;

    @InitValue("true")
    @Example("true")
    @Property("Keep alive")
    @PropertyDescription("Enables socket keep alive for this listener.")
    private Boolean keepAlive;

    @Example("true")
    @DefaultValue("false")
    @Property("Compress response")
    @PropertyDescription("If true the response is compressed before sending it to the client.")
    private Boolean compress;

    @Example("30000")
    @Property("Read timeout millis")
    @PropertyDescription("Sets the socket read timeout for this listener in milliseconds.")
    private Integer readTimeoutMillis;

    @Example("30000")
    @Property("Connection timeout millis")
    @PropertyDescription("Sets the socket connection timeout for this listener in milliseconds.")
    private Integer connectionTimeoutMillis;

    @Example("5")
    @Property("Socket backlog")
    @PropertyDescription("The number of pending connections the listener queue will hold.")
    private Integer socketBacklog;

    @Example("8192")
    @Property("Max chunk size")
    @PropertyDescription("The maximum length of the content of each chunk.")
    private Integer maxChunkSize;

    @Example("8192")
    @Property("Max headers length")
    @PropertyDescription("The maximum length of all headers.")
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
