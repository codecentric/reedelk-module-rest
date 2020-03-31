package com.reedelk.rest.openapi;

import org.json.JSONObject;

public interface OpenApiSerializable {

    JSONObject serialize(OpenApiSerializableContext context);

}
