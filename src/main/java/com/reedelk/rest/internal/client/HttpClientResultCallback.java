package com.reedelk.rest.internal.client;

import com.reedelk.rest.internal.client.response.HttpResponseMessageMapper;
import com.reedelk.rest.internal.commons.HttpHeadersAsMap;
import com.reedelk.rest.internal.commons.IsSuccessfulStatus;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.List;
import java.util.TreeMap;

import static com.reedelk.rest.internal.commons.Messages.RestClient.REQUEST_CANCELLED;
import static com.reedelk.rest.internal.commons.Messages.RestClient.REQUEST_FAILED;
import static com.reedelk.runtime.api.commons.StringUtils.isNotBlank;

public class HttpClientResultCallback implements FutureCallback<HttpResponse> {

    private final URI uri;
    private final OnResult callback;
    private final FlowContext flowContext;
    private final DynamicString target;
    private final Message originalMessage;
    private final ScriptEngineService scriptEngine;

    public HttpClientResultCallback(URI uri, FlowContext flowContext, Message message, DynamicString target, OnResult callback, ScriptEngineService scriptEngine) {
        this.uri = uri;
        this.target = target;
        this.callback = callback;
        this.originalMessage = message;
        this.flowContext = flowContext;
        this.scriptEngine = scriptEngine;
    }

    @Override
    public void completed(HttpResponse response) {
        try {
            StatusLine statusLine = response.getStatusLine();

            if (IsSuccessfulStatus.status(statusLine.getStatusCode())) {

                // If the target variable has been set, we assign to a context variable
                // the result of the HTTP response and we return the original message.
                if (target != null && isNotBlank(target.value())) {
                    Object responseContent = HttpResponseMessageMapper.mapBody(response);
                    scriptEngine.evaluate(target, flowContext, originalMessage)
                            .ifPresent(contextVariableName -> flowContext.put(contextVariableName, responseContent));
                    callback.onResult(flowContext, originalMessage);

                } else {
                    Message message = HttpResponseMessageMapper.map(response);
                    callback.onResult(flowContext, message);
                }

            } else {
                // If the response is not successful (e.g >= 200 && < 300) we throw an exception.
                HttpEntity finalEntity = HttpResponseMessageMapper.applyDecompressStrategyFrom(response.getEntity());
                TreeMap<String, List<String>> headers = HttpHeadersAsMap.of(response.getAllHeaders());
                byte[] bytes = EntityUtils.toByteArray(finalEntity);

                HttpClientResponseException exception = new HttpClientResponseException(
                        statusLine.getStatusCode(),
                        statusLine.getReasonPhrase(),
                        headers,
                        bytes);

                callback.onError(flowContext, exception);
            }

        } catch (Throwable thrown) {
            callback.onError(flowContext, thrown);
        }
    }

    @Override
    public void failed(Exception ex) {
        String errorMessage = StackTraceUtils.rootCauseMessageOf(ex);
        ESBException exception = new ESBException(REQUEST_FAILED.format(uri.toString(), errorMessage), ex);
        callback.onError(flowContext, exception);
    }

    @Override
    public void cancelled() {
        ESBException exception = new ESBException(REQUEST_CANCELLED.format(uri.toString()));
        callback.onError(flowContext, exception);
    }
}
