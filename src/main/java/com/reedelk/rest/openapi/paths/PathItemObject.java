package com.reedelk.rest.openapi.paths;

import com.reedelk.rest.commons.JsonObjectFactory;
import com.reedelk.rest.openapi.Serializable;
import org.json.JSONObject;

public class PathItemObject implements Serializable {

    private String $ref;
    private String summary;
    private String description;
    private OperationObject get;
    private OperationObject put;
    private OperationObject post;
    private OperationObject delete;
    private OperationObject options;
    private OperationObject head;

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OperationObject getGet() {
        return get;
    }

    public void setGet(OperationObject get) {
        this.get = get;
    }

    public OperationObject getPut() {
        return put;
    }

    public void setPut(OperationObject put) {
        this.put = put;
    }

    public OperationObject getPost() {
        return post;
    }

    public void setPost(OperationObject post) {
        this.post = post;
    }

    public OperationObject getDelete() {
        return delete;
    }

    public void setDelete(OperationObject delete) {
        this.delete = delete;
    }

    public OperationObject getOptions() {
        return options;
    }

    public void setOptions(OperationObject options) {
        this.options = options;
    }

    public OperationObject getHead() {
        return head;
    }

    public void setHead(OperationObject head) {
        this.head = head;
    }

    @Override
    public JSONObject serialize() {
        JSONObject pathItem = JsonObjectFactory.newJSONObject();
        pathItem.put("$ref", $ref);
        pathItem.put("summary", summary);
        pathItem.put("description", description);

        if (get != null) {
            pathItem.put("get", get.serialize());
        }
        if (put != null) {
            pathItem.put("put", put.serialize());
        }
        if (post != null) {
            pathItem.put("post", post.serialize());
        }
        if (delete != null) {
            pathItem.put("delete", delete.serialize());
        }
        if (options != null) {
            pathItem.put("options", options.serialize());
        }
        if (head != null) {
            pathItem.put("head", head.serialize());
        }
        return pathItem;
    }
}
