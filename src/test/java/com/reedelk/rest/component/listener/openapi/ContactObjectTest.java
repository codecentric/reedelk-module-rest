package com.reedelk.rest.component.listener.openapi;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class ContactObjectTest {

    @Test
    void shouldCorrectlySerializeContactWithAllProperties() {
        // Given
        ContactObject contact = new ContactObject();
        contact.setName("API Support");
        contact.setUrl("http://www.example.com/support");
        contact.setEmail("support@example.com");

        // When
        String serialized = contact.serialize().toString(2);

        // Then
        String expected = JSONS.ContactObject.WithAllProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }

    @Test
    void shouldCorrectlySerializeContactWithRequiredValues() {
        // Given
        ContactObject contact = new ContactObject();

        // When
        String serialized = contact.serialize().toString(2);

        // Then (expect empty, because there are no required properties for contact)
        String expected = JSONS.ContactObject.WithDefaultProperties.string();
        JSONAssert.assertEquals(expected, serialized, JSONCompareMode.STRICT);
    }
}
