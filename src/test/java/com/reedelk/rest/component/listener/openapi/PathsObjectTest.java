package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;

class PathsObjectTest extends AbstractOpenApiSerializableTest {

    @Test
    void shouldCorrectlySerializePathsWithDefault() {
        // Given
        PathsObject paths = new PathsObject();

        // Expect
        assertSerializedCorrectly(paths, OpenApiJsons.PathsObject.WithDefaultPaths);
    }
}
