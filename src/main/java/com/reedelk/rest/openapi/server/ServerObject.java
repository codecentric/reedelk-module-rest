package com.reedelk.rest.openapi.server;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class ServerObject implements Serializable {

    private String url;
    private String description;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JSONObject serialize() {
        JSONObject server = JsonObjectFactory.newJSONObject();
        server.put("url", url);
        server.put("description", description);
        return server;
    }
}
