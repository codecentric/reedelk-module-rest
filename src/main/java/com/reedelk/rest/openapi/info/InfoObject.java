package com.reedelk.rest.openapi.info;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class InfoObject implements Serializable {

    private String title;
    private String version;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JSONObject serialize() {
        JSONObject info = JsonObjectFactory.newJSONObject();
        info.put("title", title);
        info.put("version", version);
        info.put("description", description);
        return info;
    }
}
