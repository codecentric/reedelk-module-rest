package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class InfoObjectTest {

    @Test
    void shouldCorrectlySerializeInfoWithAllProperties() {
        // Given
        ContactObject contact = new ContactObject();
        contact.setName("API Support");
        contact.setUrl("http://www.example.com/support");
        contact.setEmail("support@example.com");

        LicenseObject license = new LicenseObject();
        license.setName("Apache 2.0");
        license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");

        InfoObject info = new InfoObject();
        info.setVersion("1.0.1");
        info.setTitle("Sample Pet Store App");
        info.setTermsOfService("http://example.com/terms/");
        info.setDescription("This is a sample server for a pet store.");
        info.setContact(contact);
        info.setLicense(license);

        // When
        String serialized = info.serialize().toString(2);

        // Then
        String expected = JSONS.InfoObject.WithAllProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }

    @Test
    void shouldCorrectlySerializeInfoWithRequiredValues() {
        // Given
        InfoObject info = new InfoObject();

        // When
        String serialized = info.serialize().toString(2);

        // Then
        String expected = JSONS.InfoObject.WithDefaultProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }
}
