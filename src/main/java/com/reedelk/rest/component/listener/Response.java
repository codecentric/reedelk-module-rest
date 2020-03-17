package com.reedelk.rest.component.listener;

import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicInteger;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = Response.class, scope = PROTOTYPE)
public class Response implements Implementor {

    @Property("Body")
    @InitValue("#[message.payload()]")
    @Hint("content body text")
    @Description("The body of the response might be a static or a dynamic value.")
    private DynamicByteArray body;

    @Property("Status")
    @Hint("200")
    @InitValue("200")
    @Description("The status code of the response might be a static or a dynamic value, e.g. could be a variable defined in the flow context: <i>context.myResponseStatus</i>.")
    private DynamicInteger status;

    @Property("Additional Headers")
    @KeyName("Header Name")
    @ValueName("Header Value")
    @Description("Additional headers to be set in the HTTP response.")
    private DynamicStringMap headers = DynamicStringMap.empty();

    public DynamicByteArray getBody() {
        return body;
    }

    public void setBody(DynamicByteArray body) {
        this.body = body;
    }

    public DynamicInteger getStatus() {
        return status;
    }

    public void setStatus(DynamicInteger status) {
        this.status = status;
    }

    public DynamicStringMap getHeaders() {
        return headers;
    }

    public void setHeaders(DynamicStringMap headers) {
        this.headers = headers;
    }
}
