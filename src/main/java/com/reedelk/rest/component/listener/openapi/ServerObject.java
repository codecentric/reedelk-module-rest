package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.KeyName;
import com.reedelk.runtime.api.annotation.Property;
import com.reedelk.runtime.api.annotation.TabGroup;
import com.reedelk.runtime.api.annotation.ValueName;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = ServerObject.class, scope = PROTOTYPE)
public class ServerObject implements Implementor, OpenApiSerializable {

    @Property("URL")
    private String url;

    @Property("Description")
    private String description;

    @Property("URL Variables")
    @TabGroup("URL Variables")
    @KeyName("Variable Name")
    @ValueName("Variable Definition")
    private Map<String, ServerVariableObject> variables;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, ServerVariableObject> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, ServerVariableObject> variables) {
        this.variables = variables;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public JSONObject serialize() {
        JSONObject server = JsonObjectFactory.newJSONObject();
        server.put("url", url);
        server.put("description", description);
        return server;
    }
}
