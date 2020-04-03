package com.reedelk.rest.internal.openapi;

import org.json.JSONObject;

public interface OpenApiSerializable {

    JSONObject serialize(OpenApiSerializableContext context);

}
