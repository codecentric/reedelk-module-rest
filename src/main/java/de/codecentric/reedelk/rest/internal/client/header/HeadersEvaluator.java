package de.codecentric.reedelk.rest.internal.client.header;

import de.codecentric.reedelk.rest.internal.commons.ContentType;
import de.codecentric.reedelk.runtime.api.commons.ScriptUtils;
import de.codecentric.reedelk.runtime.api.flow.FlowContext;
import de.codecentric.reedelk.runtime.api.message.Message;
import de.codecentric.reedelk.runtime.api.script.ScriptEngineService;
import de.codecentric.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import de.codecentric.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import de.codecentric.reedelk.rest.internal.commons.HttpHeader;

import java.util.HashMap;
import java.util.Map;

public class HeadersEvaluator {

    private ScriptEngineService scriptEngine;
    private DynamicStringMap userHeaders;
    private DynamicObject body;

    private HeadersEvaluator(ScriptEngineService scriptEngine,
                             DynamicStringMap userHeaders,
                             DynamicObject body) {
        this.scriptEngine = scriptEngine;
        this.userHeaders = userHeaders;
        this.body = body;
    }

    public HeaderProvider provider(Message message, FlowContext flowContext) {
        Map<String, String> headers = new HashMap<>();

        if (ScriptUtils.isEvaluateMessagePayload(body)) {
            ContentType.from(message)
                    .ifPresent(contentType -> headers.put(HttpHeader.CONTENT_TYPE, contentType));
        }

        if (!userHeaders.isEmpty()) {
            // User-defined headers: interpret and add them
            Map<String, String> evaluatedHeaders = scriptEngine.evaluate(userHeaders, flowContext, message);
            headers.putAll(evaluatedHeaders);
        }

        return () -> headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ScriptEngineService scriptEngine;
        private DynamicStringMap headers;
        private DynamicObject body;

        public Builder scriptEngine(ScriptEngineService scriptEngine) {
            this.scriptEngine = scriptEngine;
            return this;
        }

        public Builder headers(DynamicStringMap headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(DynamicObject body) {
            this.body = body;
            return this;
        }

        public HeadersEvaluator build() {
            return new HeadersEvaluator(scriptEngine, headers, body);
        }
    }
}
