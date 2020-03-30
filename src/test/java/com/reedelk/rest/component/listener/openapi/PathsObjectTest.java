package com.reedelk.rest.component.listener.openapi;

import com.reedelk.rest.commons.RestMethod;
import org.junit.jupiter.api.Test;

class PathsObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializePathsWithDefault() {
        // Given
        PathsObject paths = new PathsObject();

        // Expect
        assertSerializedCorrectly(paths, OpenApiJsons.PathsObject.WithDefaultPaths);
    }

    @Test
    void shouldCorrectlySerializePathsWithDefaultOperationObjectForPath() {
        // Given
        PathsObject paths = new PathsObject();
        paths.add("/mypath", RestMethod.POST);

        // Expect
        assertSerializedCorrectly(paths, OpenApiJsons.PathsObject.WithDefaultOperation);
    }
}
