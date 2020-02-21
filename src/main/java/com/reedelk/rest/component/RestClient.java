package com.reedelk.rest.component;

import com.reedelk.rest.client.HttpClient;
import com.reedelk.rest.client.HttpClientFactory;
import com.reedelk.rest.client.HttpClientResultCallback;
import com.reedelk.rest.client.body.BodyEvaluator;
import com.reedelk.rest.client.body.BodyProvider;
import com.reedelk.rest.client.header.HeaderProvider;
import com.reedelk.rest.client.header.HeadersEvaluator;
import com.reedelk.rest.client.strategy.ExecutionStrategyBuilder;
import com.reedelk.rest.client.strategy.Strategy;
import com.reedelk.rest.client.uri.UriEvaluator;
import com.reedelk.rest.client.uri.UriProvider;
import com.reedelk.rest.commons.RestMethod;
import com.reedelk.rest.configuration.StreamingMode;
import com.reedelk.rest.configuration.client.AdvancedConfiguration;
import com.reedelk.rest.configuration.client.ClientConfiguration;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.component.ProcessorAsync;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.net.URI;

import static java.util.Objects.requireNonNull;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ModuleComponent("REST Client")
@Description("Use this component to make a REST API Call. " +
                "Supported REST methods are: GET, POST, PUT, DELETE, HEAD, OPTIONS. " +
                "The base REST Client configuration can be shared across multiple REST clients in " +
                "case there is a common remote resource to make different requests to. The components supports " +
                "basic an digest authentication with the remote server.")
@Component(service = RestClient.class, scope = PROTOTYPE)
public class RestClient implements ProcessorAsync {

    @Reference
    private ScriptEngineService scriptEngine;
    @Reference
    private HttpClientFactory clientFactory;
    @Reference
    private ConverterService converterService;

    @Example("POST")
    @InitValue("GET")
    @DefaultValue("GET")
    @Property("Method")
    @Description("The REST method to be used to make the request. Possible values are: GET, POST, PUT, DELETE, HEAD, OPTIONS.")
    private RestMethod method;

    @Property("Client config")
    private ClientConfiguration configuration;

    @Hint("https://api.example.com")
    @Example("http://api.example.com/orders")
    @When(propertyName = "configuration", propertyValue = When.NULL)
    @When(propertyName = "configuration", propertyValue = "{'ref': '" + When.BLANK + "'}")
    @Property("Base URL")
    @Description("The base URL of the HTTP request. It may include a static request path.")
    private String baseURL;

    @Hint("/resource/{id}")
    @Example("/resource/{id}/{group}")
    @Property("Path")
    @Description("The request path might contain parameters placeholders which will be bound to the values defined in the <i>Headers and parameters</i> > <i>Path params</i> map.")
    private String path;

    @Hint("payload")
    @DefaultValue("<code>message.payload()</code>")
    @Example("<code>context.myCustomPayload</code>")
    @InitValue("#[message.payload()]")
    @When(propertyName = "method", propertyValue = "DELETE")
    @When(propertyName = "method", propertyValue = "POST")
    @When(propertyName = "method", propertyValue = "PUT")
    @Property("Body")
    @Description("Sets the payload of the HTTP request. It could be a dynamic or a static value.")
    private DynamicObject body;

    @DefaultValue("AUTO")
    @Example("ALWAYS")
    @InitValue("AUTO")
    @When(propertyName = "method", propertyValue = "DELETE")
    @When(propertyName = "method", propertyValue = "POST")
    @When(propertyName = "method", propertyValue = "PUT")
    @Property("Streaming")
    @Description("Determines the strategy type the body will be sent to the server. " +
            "When <i>Stream</i> the request body will be sent chunk by chunk without loading the entire content into memory. " +
            "When <i>None</i> the body will be loaded into memory and then sent to the server. When <i>Auto</i> the component " +
            "will inspect the content of the body to determine the best strategy to send the HTTP request data.")
    private StreamingMode streaming = StreamingMode.AUTO;

    @TabGroup("Headers and parameters")
    @Example("X-Custom-Header > <code>'X-Custom-' + message.payload() + ' value'</code>")
    @Property("Headers")
    @Description("Map of dynamic headers names > values. The values are dynamic.")
    private DynamicStringMap headers = DynamicStringMap.empty();

    @TabGroup("Headers and parameters")
    @Example("id > <code>message.payload()</code>")
    @Property("Path params")
    @Description("Map of request path parameters names > values. The values are dynamic.")
    private DynamicStringMap pathParameters = DynamicStringMap.empty();

    @TabGroup("Headers and parameters")
    @Example("id > <code>message.payload()</code>")
    @Property("Query params")
    @Description("Map of request query parameters names > values. The values are dynamic.")
    private DynamicStringMap queryParameters = DynamicStringMap.empty();

    @Property("Advanced configuration")
    private AdvancedConfiguration advancedConfiguration;

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

        HttpClientResultCallback resultCallback = new HttpClientResultCallback(uri, flowContext, callback);

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
                .advancedConfig(advancedConfiguration)
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
            requireNonNull(baseURL, "RestClient base URL must be defined");
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

    public void setConfiguration(ClientConfiguration configuration) {
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

    public void setAdvancedConfiguration(AdvancedConfiguration advancedConfiguration) {
        this.advancedConfiguration = advancedConfiguration;
    }
}
