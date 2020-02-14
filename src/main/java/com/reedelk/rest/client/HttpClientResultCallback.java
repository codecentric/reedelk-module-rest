package com.reedelk.rest.client;

import com.reedelk.rest.client.response.HttpResponseMessageMapper;
import com.reedelk.rest.commons.IsSuccessfulStatus;
import com.reedelk.runtime.api.commons.StackTraceUtils;
import com.reedelk.runtime.api.component.OnResult;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;

import java.net.URI;

import static com.reedelk.rest.commons.Messages.RestClient.REQUEST_CANCELLED;
import static com.reedelk.rest.commons.Messages.RestClient.REQUEST_FAILED;

public class HttpClientResultCallback implements FutureCallback<HttpResponse> {

    private final URI uri;
    private final OnResult callback;
    private final FlowContext flowContext;

    public HttpClientResultCallback(URI uri, FlowContext flowContext, OnResult callback) {
        this.uri = uri;
        this.callback = callback;
        this.flowContext = flowContext;
    }

    @Override
    public void completed(HttpResponse response) {
        try {
            StatusLine statusLine = response.getStatusLine();

            if (IsSuccessfulStatus.status(statusLine.getStatusCode())) {
                Message message = HttpResponseMessageMapper.map(response);
                callback.onResult(flowContext, message);

            } else {
                // If the response is not successful (e.g >= 200 && < 300) we throw an exception.
                HttpEntity finalEntity = HttpResponseMessageMapper.applyDecompressStrategyFrom(response.getEntity());
                byte[] bytes = EntityUtils.toByteArray(finalEntity);

                HttpClientResponseException exception =
                        new HttpClientResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase(), bytes);
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
