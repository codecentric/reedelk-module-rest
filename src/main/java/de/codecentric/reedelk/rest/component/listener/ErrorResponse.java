package de.codecentric.reedelk.rest.component.listener;

import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.Implementor;
import de.codecentric.reedelk.runtime.api.exception.ExceptionType;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicInteger;
import org.osgi.service.component.annotations.Component;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Collapsible
@Component(service = ErrorResponse.class, scope = PROTOTYPE)
public class ErrorResponse implements Implementor {

    @Property("Error body")
    @Hint("error body text")
    @InitValue("#[error]")
    @ScriptSignature(arguments = {"context", "error"}, types = {FlowContext.class, ExceptionType.class})
    @Description("The body of the error response might be a static or a dynamic value.")
    private DynamicByteArray body;

    @Property("Error status")
    @Hint("500")
    @InitValue("500")
    @ScriptSignature(arguments = {"context", "error"}, types = {FlowContext.class, ExceptionType.class})
    @Description("The status code of the error response might be a static or a dynamic value, " +
            "e.g. could be a variable defined in the flow context: <code>context.myErrorResponseStatus</code>.")
    private DynamicInteger status;

    @Property("Error response headers")
    @TabGroup("Error response headers")
    @KeyName("Header Name")
    @ValueName("Header Value")
    @ScriptSignature(arguments = {"context", "error"}, types = {FlowContext.class, ExceptionType.class})
    @Description("Additional headers to be set in the HTTP error response.")
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
