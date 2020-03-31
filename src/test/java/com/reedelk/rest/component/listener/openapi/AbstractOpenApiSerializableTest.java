package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.rest.openapi.OpenApiSerializableContext;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

abstract class AbstractOpenApiSerializableTest {

    protected void assertSerializedCorrectly(OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        ComponentsObject componentsObject = new ComponentsObject(); // TODO: This one should be mock?
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        String actualJson = serializable.serialize(context).toString(2);
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actualJson, STRICT);
    }
}
