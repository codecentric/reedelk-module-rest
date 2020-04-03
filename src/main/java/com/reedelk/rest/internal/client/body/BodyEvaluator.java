package com.reedelk.rest.internal.client.body;

import com.reedelk.rest.internal.commons.RestMethod;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;

public class BodyEvaluator {

    private final BodyProvider bodyProvider;

    private BodyEvaluator(BodyProvider bodyProvider) {
        this.bodyProvider = bodyProvider;
    }

    public static Builder builder() {
        return new Builder();
    }

    public BodyProvider provider() {
        return bodyProvider;
    }

    public static class Builder {

        private ScriptEngineService scriptEngine;
        private ConverterService converter;
        private RestMethod method;
        private DynamicObject body;

        public Builder body(DynamicObject body) {
            this.body = body;
            return this;
        }

        public Builder method(RestMethod method) {
            this.method = method;
            return this;
        }

        public Builder converter(ConverterService converterService) {
            this.converter = converterService;
            return this;
        }

        public Builder scriptEngine(ScriptEngineService scriptEngine) {
            this.scriptEngine = scriptEngine;
            return this;
        }

        public BodyEvaluator build() {
            BodyProvider provider = method.hasBody() ?
                    new DefaultBodyProvider(scriptEngine, converter, body) :
                    EmptyBodyProvider.INSTANCE;
            return new BodyEvaluator(provider);
        }
    }
}
