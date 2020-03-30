package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.openapi.OpenApiSerializable;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

abstract class AbstractOpenApiSerializableTest {

    protected void assertSerializedCorrectly(OpenApiSerializable serializable, OpenApiJsons.Provider expected) {
        String actualJson = serializable.serialize().toString(2);
        String expectedJson = expected.string();
        JSONAssert.assertEquals(expectedJson, actualJson, STRICT);
    }
}
