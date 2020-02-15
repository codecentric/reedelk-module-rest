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
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.net.URI;

import static java.util.Objects.requireNonNull;
import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@ESBComponent("REST Client")
@Component(service = RestClient.class, scope = PROTOTYPE)
public class RestClient implements ProcessorAsync {

    @Reference
    private ScriptEngineService scriptEngine;
    @Reference
    private HttpClientFactory clientFactory;

    @Property("Method")
    @Default("GET")
    private RestMethod method;

    @Property("Client config")
    private ClientConfiguration configuration;

    @Property("Base URL")
    @PropertyInfo("The base URL of the HTTP request. " +
            "It may include a static request path " +
            "e.g: http://api.example.com/resource1")
    @Hint("https://api.example.com")
    @When(propertyName = "configuration", propertyValue = When.NULL)
    @When(propertyName = "configuration", propertyValue = "{'ref': '" + When.BLANK + "'}")
    private String baseURL;

    @Property("Multipart")
    @When(propertyName = "method", propertyValue = "POST")
    private Boolean multipart;

    @Property("Path")
    @Hint("/resource/{id}")
    @PropertyInfo("The request path might contain parameters placeholders which will be bound to the values defined in the <i>Headers and parameters</i> > <i>Path params</i> map, e.g: /resource/{id}/{group}.")
    private String path;

    @Property("Body")
    @Hint("payload")
    @Default("#[message.payload()]")
    @When(propertyName = "method", propertyValue = "DELETE")
    @When(propertyName = "method", propertyValue = "POST")
    @When(propertyName = "method", propertyValue = "PUT")
    @PropertyInfo("Sets the payload of the HTTP request. It could be a dynamic or a static value.")
    private DynamicByteArray body;

    @Property("Streaming")
    @Default("AUTO")
    @When(propertyName = "method", propertyValue = "DELETE")
    @When(propertyName = "method", propertyValue = "POST")
    @When(propertyName = "method", propertyValue = "PUT")
    @PropertyInfo("Determines the strategy type the body will be sent to the server. " +
            "When <i>Stream</i> the request body will be sent chunk by chunk without loading the entire content into memory. " +
            "When <i>None</i> the body will be loaded into memory and then sent to the server. When <i>Auto</i> the component " +
            "will inspect the content of the body to determine the best strategy to send the HTTP request data.")
    private StreamingMode streaming = StreamingMode.AUTO;

    @TabGroup("Headers and parameters")
    @Property("Headers")
    private DynamicStringMap headers = DynamicStringMap.empty();

    @TabGroup("Headers and parameters")
    @Property("Path params")
    private DynamicStringMap pathParameters = DynamicStringMap.empty();

    @TabGroup("Headers and parameters")
    @Property("Query params")
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
                .multipart(multipart)
                .streaming(streaming)
                .method(method)
                .build();

        // Init body evaluator
        bodyEvaluator = BodyEvaluator.builder()
                .scriptEngine(scriptEngine)
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

    public void setBody(DynamicByteArray body) {
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

    public void setMultipart(Boolean multipart) {
        this.multipart = multipart;
    }
}
