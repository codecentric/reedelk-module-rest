package com.reedelk.rest.client;

import com.reedelk.rest.client.response.HttpResponseMessageMapper;
import com.reedelk.rest.commons.IsSuccessfulStatus;
import com.reedelk.runtime.api.exception.ESBException;
import com.reedelk.runtime.api.message.Message;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;

import static com.reedelk.rest.commons.Messages.RestClient.REQUEST_CANCELLED;
import static com.reedelk.rest.commons.Messages.RestClient.REQUEST_FAILED;

public class HttpClientResultCallback implements FutureCallback<HttpResponse> {

    private final URI requestURI;
    private HttpResponse result;
    private Exception exception;
    private boolean cancelled = false;
    private boolean failed = false;

    public HttpClientResultCallback(URI requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public void completed(HttpResponse result) {
        this.result = result;
    }

    @Override
    public void failed(Exception exception) {
        this.failed = true;
        this.exception = exception;
    }

    @Override
    public void cancelled() {
        this.cancelled = true;
    }


    public Message get() {
        if (cancelled) {
            throw new ESBException(REQUEST_CANCELLED.format(requestURI));
        }
        if (failed) {
            throw new ESBException(REQUEST_FAILED.format(requestURI, exception.getMessage()), exception);
        }

        // If the response is not successful we throw an exception.
        if (IsSuccessfulStatus.status(result.getStatusLine().getStatusCode())) {
            return HttpResponseMessageMapper.map(result);

        } else {
            try {
                byte[] bytes = EntityUtils.toByteArray(result.getEntity());
                throw new HttpClientResponseException(result, Flux.just(bytes));
            } catch (IOException e) {
                throw new ESBException("Stream culd not be consumed");
            }
        }
    }
}
