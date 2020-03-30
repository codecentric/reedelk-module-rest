package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.runtime.api.annotation.*;
import com.reedelk.runtime.api.component.Implementor;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

import static org.osgi.service.component.annotations.ServiceScope.PROTOTYPE;

@Component(service = ServerObject.class, scope = PROTOTYPE)
public class ServerObject implements Implementor, OpenApiSerializable {

    @Property("URL")
    @Hint("https://development.gigantic-server.com/v1")
    @Example("https://development.gigantic-server.com/v1")
    @DefaultValue("/")
    @Description(" A URL to the target host. This URL supports Server Variables and MAY be relative, " +
            "to indicate that the host location is relative to the location where the OpenAPI document is being served. " +
            "Variable substitutions will be made when a variable is named in <i>{brackets}</i>.")
    private String url = "/"; // Default server URL: https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md#schema

    @Property("Description")
    @Hint("Development server")
    @Example("Development server")
    @Description("An optional string describing the host designated by the URL.")
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
        JSONObject serialized = JsonObjectFactory.newJSONObject();
        set(serialized, "url", url);
        set(serialized, "description", description);
        if (variables != null && !variables.isEmpty()) {
            JSONObject serializedVariables = JsonObjectFactory.newJSONObject();
            variables.forEach((key, serverVariable) -> set(serializedVariables, key, serverVariable));
            set(serialized, "variables", serializedVariables);
        }
        return serialized;
    }
}