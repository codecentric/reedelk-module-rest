package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class LicenseObjectTest {

    @Test
    void shouldCorrectlySerializeLicenseWithAllProperties() {
        // Given
        LicenseObject license = new LicenseObject();
        license.setName("Apache 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

        // When
        String serialized = license.serialize().toString(2);

        // Then
        String expected = JSONS.LicenseObject.WithAllProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }

    @Test
    void shouldCorrectlySerializeLicenseWithRequiredValues() {
        // Given
        LicenseObject license = new LicenseObject();

        // When
        String serialized = license.serialize().toString(2);

        // Then
        String expected = JSONS.LicenseObject.WithDefaultProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }
}
