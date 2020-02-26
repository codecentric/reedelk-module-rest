package com.reedelk.rest.script;

import com.reedelk.runtime.api.script.ScriptSource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableList;


public class RestScriptModules implements ScriptSource {

    private final long moduleId;

    public RestScriptModules(long moduleId) {
        this.moduleId = moduleId;
    }


    @Override
    public Map<String, Object> bindings() {
        Map<String, Object> bindings = new HashMap<>();
        bindings.put("builder", new MultipartBuilder());
        return bindings;
    }

    @Override
    public Collection<String> scriptModuleNames() {
        return unmodifiableList(Collections.singletonList("MultipartBuilder"));
    }

    @Override
    public long moduleId() {
        return moduleId;
    }

    @Override
    public String resource() {
        return "/function/rest-javascript-functions.js";
    }
}
