package de.codecentric.reedelk.rest.component.client;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = BufferConfiguration.class, scope = PROTOTYPE)
public class BufferConfiguration implements Implementor {

    @Property("Response buffer size")
    @Hint("16384") // 16 * 1024
    @Example("16384")
    @DefaultValue("16384")
    @Description("The buffer size to be used while receiving the HTTP response.")
    private Integer responseBufferSize;

    @Property("Request buffer size")
    @Hint("16384") // 16 * 1024
    @Example("16384")
    @DefaultValue("16384")
    @Description("The buffer size to be used while sending the HTTP request.")
    private Integer requestBufferSize;

    public Integer getResponseBufferSize() {
        return responseBufferSize;
    }

    public void setResponseBufferSize(Integer responseBufferSize) {
        this.responseBufferSize = responseBufferSize;
    }

    public Integer getRequestBufferSize() {
        return requestBufferSize;
    }

    public void setRequestBufferSize(Integer requestBufferSize) {
        this.requestBufferSize = requestBufferSize;
    }
}
