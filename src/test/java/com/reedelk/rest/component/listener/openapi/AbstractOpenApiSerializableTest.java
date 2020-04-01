package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.openapi.OpenApiSerializable;
import com.reedelk.rest.openapi.OpenApiSerializableContext;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public abstract class AbstractOpenApiSerializableTest {

    protected void assertSerializedCorrectly(OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        ComponentsObject componentsObject = new ComponentsObject();
        OpenApiSerializableContext context = new OpenApiSerializableContext(componentsObject);
        String actualJson = serializable.serialize(context).toString(2);
        assertSerializedCorrectly(actualJson, expected);
    }

    protected void assertSerializedCorrectly(OpenApiSerializableContext context, OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        String actualJson = serializable.serialize(context).toString(2);
        assertSerializedCorrectly(actualJson, expected);
    }

    protected void assertSerializedCorrectly(String actual, OpenApiJsons.Provider expected) {
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actual, STRICT);
    }
}
