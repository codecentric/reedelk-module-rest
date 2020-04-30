package com.reedelk.rest.internal.attribute;

import com.reedelk.rest.internal.commons.HttpHeadersAsMap;
import com.reedelk.runtime.api.annotation.Type;
import com.reedelk.runtime.api.annotation.TypeProperty;
import com.reedelk.runtime.api.message.MessageAttributes;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import java.util.Map;

import static com.reedelk.rest.internal.attribute.RESTClientAttributes.*;

@Type
@TypeProperty(name = REASON_PHRASE, type = String.class)
@TypeProperty(name = STATUS_CODE, type = int.class)
@TypeProperty(name = HEADERS, type = Map.class)
public class RESTClientAttributes extends MessageAttributes {

    public static final String REASON_PHRASE = "reasonPhrase";
    public static final String STATUS_CODE = "statusCode";
    public static final String HEADERS =  "headers";

    public RESTClientAttributes(HttpResponse response) {
        StatusLine statusLine = response.getStatusLine();
        put(HEADERS, HttpHeadersAsMap.of(response.getAllHeaders()));
        put(REASON_PHRASE, statusLine.getReasonPhrase());
        put(STATUS_CODE, statusLine.getStatusCode());
    }
}

