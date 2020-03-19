package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class ParameterObject implements Serializable {

    private String in;
    private String name;
    private String description;

    private Boolean required;
    private Boolean deprecated;

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = JsonObjectFactory.newJSONObject();
        if (in != null) {
            object.put("in", in);
        }
        if (name != null) {
            object.put("name", name);
        }
        if (description != null) {
            object.put("description", deprecated);
        }
        if (required != null) {
            object.put("required", required);
        }
        if (deprecated != null) {
            object.put("deprecated", deprecated);
        }
        return object;
    }
}
