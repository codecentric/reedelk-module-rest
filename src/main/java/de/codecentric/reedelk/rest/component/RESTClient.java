package de.codecentric.reedelk.rest.component;

import de.codecentric.reedelk.rest.component.client.BufferConfiguration;
import de.codecentric.reedelk.rest.internal.attribute.RESTClientAttributes;
import de.codecentric.reedelk.rest.internal.client.HttpClient;
import de.codecentric.reedelk.rest.internal.client.HttpClientFactory;
import de.codecentric.reedelk.rest.internal.client.HttpClientResultCallback;
import de.codecentric.reedelk.rest.internal.client.body.BodyEvaluator;
import de.codecentric.reedelk.rest.internal.client.body.BodyProvider;
import de.codecentric.reedelk.rest.internal.client.header.HeaderProvider;
import de.codecentric.reedelk.rest.internal.client.header.HeadersEvaluator;
import de.codecentric.reedelk.rest.internal.client.strategy.ExecutionStrategyBuilder;
import de.codecentric.reedelk.rest.internal.client.strategy.Strategy;
import de.codecentric.reedelk.rest.internal.client.uri.UriEvaluator;
import de.codecentric.reedelk.rest.internal.client.uri.UriProvider;
import de.codecentric.reedelk.rest.internal.commons.RestMethod;
import de.codecentric.reedelk.rest.internal.commons.StreamingMode;
import de.codecentric.reedelk.runtime.api.annotation.*;
import de.codecentric.reedelk.runtime.api.component.OnResult;
import de.codecentric.reedelk.runtime.api.component.ProcessorAsync;
import de.codecentric.reedelk.runtime.api.converter.ConverterService;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.net.URI;

import static java.util.Objects.requireNonNull;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("REST Client")
@ComponentOutput(
        attributes = RESTClientAttributes.class,
        payload = { byte[].class, String.class },
        description = "The data content of the HTTP response")
@ComponentInput(
        payload = { byte[].class, String.class },
        description = "The data to be sent in the HTTP request for POST, PUT, DELETE requests.")
@Description("Use this component to make a REST API Call. " +
                "Supported REST methods are: GET, POST, PUT, DELETE, HEAD, OPTIONS. " +
                "The base REST Client configuration can be shared across multiple REST clients in " +
                "case there is a common remote resource to make different requests to. The components supports " +
                "basic an digest authentication with the remote server.")
@Component(service = RESTClient.class, scope = PROTOTYPE)
public class RESTClient implements ProcessorAsync {

    @DialogTitle("REST Client Configuration")
    @Property("Configuration")
    private RESTClientConfiguration configuration;

    @Property("Base URL")
    @Hint("https://api.example.com")
    @Example("http://api.example.com/orders")
    @When(propertyName = "configuration", propertyValue = When.NULL)
    @When(propertyName = "configuration", propertyValue = "{'ref': '" + When.BLANK + "'}")
    @Description("The base URL of the HTTP request. It may include a static request path.")
    private String baseURL;

    @Property("Path")
    @Hint("/resource/{id}")
    @Example("/resource/{id}/{group}")
    @Description("The request path might contain parameters placeholders which will be bound to the values defined in the <i>Headers and parameters</i> > <i>Path params</i> map.")
    private String path;

    @Property("Method")
    @Example("POST")
    @InitValue("GET")
    @DefaultValue("GET")
    @Description("The REST method to be used to make the request. Possible values are: GET, POST, PUT, DELETE, HEAD, OPTIONS.")
    private RestMethod method;

    @Property("Body")
    @Hint("payload")
    @InitValue("#[message.payload()]")
    @DefaultValue("<code>message.payload()</code>")
    @Example("<code>context.myCustomPayload</code>")
    @When(propertyName = "method", propertyValue = "DELETE")
    @When(propertyName = "method", propertyValue = "POST")
    @When(propertyName = "method", propertyValue = "PUT")
    @Description("Sets the payload of the HTTP request. It could be a dynamic or a static value.")
    private DynamicObject body;

    @Property("Headers")
    @KeyName("Header Name")
    @ValueName("Header Value")
    @TabGroup("Headers and parameters")
    @Example("X-Custom-Header > <code>'X-Custom-' + message.payload() + ' value'</code>")
    @Description("Map of dynamic headers names > values. The values are dynamic.")
    private DynamicStringMap headers = DynamicStringMap.empty();

    @Property("Path params")
    @KeyName("Path Param Name")
    @ValueName("Path Param Value")
    @TabGroup("Headers and parameters")
    @Example("id > <code>message.payload()</code>")
    @Description("Map of request path parameters names > values. The values are dynamic.")
    private DynamicStringMap pathParameters = DynamicStringMap.empty();

    @Property("Query params")
    @KeyName("Query Param Name")
    @ValueName("Query Param Value")
    @TabGroup("Headers and parameters")
    @Example("id > <code>message.payload()</code>")
    @Description("Map of request query parameters names > values. The values are dynamic.")
    private DynamicStringMap queryParameters = DynamicStringMap.empty();

    @Property("Target Variable")
    @Hint("myRestClientResult")
    @Example("myRestClientResult")
    @Group("Advanced")
    @Description("If the property is not empty, the result of the REST Client execution is assigned to " +
            "the context variable with the given target name instead of the message payload.")
    private String target;

    @Property("Streaming")
    @Group("Advanced")
    @Example("ALWAYS")
    @InitValue("AUTO")
    @DefaultValue("AUTO")
    @When(propertyName = "method", propertyValue = "DELETE")
    @When(propertyName = "method", propertyValue = "POST")
    @When(propertyName = "method", propertyValue = "PUT")
    @Description("Determines the strategy type the body will be sent to the server. " +
            "When <i>Stream</i> the request body will be sent chunk by chunk without loading the entire content into memory. " +
            "When <i>None</i> the body will be loaded into memory and then sent to the server. When <i>Auto</i> the component " +
            "will inspect the content of the body to determine the best strategy to send the HTTP request data.")
    private StreamingMode streaming = StreamingMode.AUTO;

    @Group("Advanced")
    @Property("Buffer Configuration")
    private BufferConfiguration bufferConfiguration;

    @Reference
    private HttpClientFactory clientFactory;
    @Reference
    private ScriptEngineService scriptEngine;
    @Reference
    private ConverterService converterService;

    private HttpClient client;
    private Strategy execution;
    private UriEvaluator uriEvaluator;
    private BodyEvaluator bodyEvaluator;
    private HeadersEvaluator headersEvaluator;

    @Override
    public void apply(FlowContext flowContext, Message message, OnResult callback) {

        BodyProvider bodyProvider = bodyEvaluator.provider();

        UriProvider uriProvider = uriEvaluator.provider(message, flowContext);

        HeaderProvider headerProvider = headersEvaluator.provider(message, flowContext);

        URI uri = uriProvider.uri();

        HttpClientResultCallback resultCallback =
                new HttpClientResultCallback(uri, flowContext, message, target, callback, scriptEngine);

        execution.execute(client, message, flowContext, uri, headerProvider, bodyProvider, resultCallback);
    }

    @Override
    public synchronized void initialize() {
        // Init uri evaluator
        uriEvaluator = UriEvaluator.builder()
                .queryParameters(queryParameters)
                .pathParameters(pathParameters)
                .configuration(configuration)
                .scriptEngine(scriptEngine)
                .baseURL(baseURL)
                .path(path)
                .build();

        // Init execution
        execution = ExecutionStrategyBuilder.builder()
                .advancedConfig(bufferConfiguration)
                .streaming(streaming)
                .method(method)
                .build();

        // Init body evaluator
        bodyEvaluator = BodyEvaluator.builder()
                .scriptEngine(scriptEngine)
                .converter(converterService)
                .method(method)
                .body(body)
                .build();

        // Headers
        headersEvaluator = HeadersEvaluator.builder()
                .scriptEngine(scriptEngine)
                .headers(headers)
                .body(body)
                .build();

        // Init rest client
        if (configuration != null) {
            client = clientFactory.create(this, configuration);
        } else {
            requireNonNull(baseURL, "RESTClient base URL must be defined");
            client = clientFactory.create();
        }
    }

    @Override
    public synchronized void dispose() {
        clientFactory.release(configuration, this, client);
        scriptEngine = null;
        uriEvaluator = null;
        bodyEvaluator = null;
        headersEvaluator = null;
        clientFactory = null;
    }

    public void setMethod(RestMethod method) {
        this.method = method;
    }

    public void setConfiguration(RESTClientConfiguration configuration) {
        this.configuration = configuration;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBody(DynamicObject body) {
        this.body = body;
    }

    public void setStreaming(StreamingMode streaming) {
        this.streaming = streaming;
    }

    public void setHeaders(DynamicStringMap headers) {
        this.headers = headers;
    }

    public void setPathParameters(DynamicStringMap pathParameters) {
        this.pathParameters = pathParameters;
    }

    public void setQueryParameters(DynamicStringMap queryParameters) {
        this.queryParameters = queryParameters;
    }

    public void setBufferConfiguration(BufferConfiguration bufferConfiguration) {
        this.bufferConfiguration = bufferConfiguration;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
