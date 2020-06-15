package com.reedelk.rest.component;

import com.reedelk.rest.component.multipart.PartDefinition;
import com.reedelk.runtime.api.commons.ImmutableMap;
import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.content.Attachment;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicmap.DynamicMap;
import com.reedelk.runtime.api.script.dynamicmap.DynamicStringMap;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicByteArray;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicString;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class MultipartMessageTest {

    @Mock
    private Message message;
    @Mock
    private FlowContext context;
    @Mock
    private ScriptEngineService mockScriptEngine;


    private MultipartMessage processor = new MultipartMessage();

    @BeforeEach
    void setUp() {
        processor.scriptEngine = mockScriptEngine;
    }

    @Test
    void shouldReturnEmptyWhenNoPartsDefinitions() {
        // When
        Message actual = processor.apply(context, message);

        // Then
        Map<String, Attachment> parts = actual.payload();
        assertThat(parts).isEqualTo(new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldReturnCorrectPartsFromDefinitions() {
        // Given
        doAnswer(invocation -> {
            DynamicValue<?> dynamicValue = invocation.getArgument(0);
            if (dynamicValue == null) return Optional.empty();
            return Optional.ofNullable(dynamicValue.value());
        }).when(mockScriptEngine).evaluate(any(DynamicValue.class), eq(context), eq(message));

        doAnswer(invocation -> {
            DynamicMap<?> dynamicMap = invocation.getArgument(0);
            if (dynamicMap == null) return Optional.empty();
            Map<String,Object> returnMap = new HashMap<>();
            dynamicMap.keySet().forEach(key -> {
                DynamicValue<?> dynamicValue = (DynamicValue<?>) dynamicMap.get(key);
                returnMap.put(key, dynamicValue.value());
            });
            return returnMap;
        }).when(mockScriptEngine).evaluate(any(DynamicMap.class), eq(context), eq(message));

        Map<String, DynamicString> attributes1 = ImmutableMap.of("filename", DynamicString.from("my-picture.jpg"));

        PartDefinition definition1 = new PartDefinition();
        definition1.setMimeType(MimeType.AsString.APPLICATION_BINARY);
        definition1.setContent(DynamicByteArray.from("content1"));
        definition1.setAttributes(DynamicStringMap.from(attributes1, new ModuleContext(10L)));

        Map<String, PartDefinition> parts = ImmutableMap.of("part1", definition1);
        processor.setParts(parts);

        // When
        Message actual = processor.apply(context, message);

        // Then
        Map<String, Attachment> partsAttachments = actual.payload();
        assertThat(partsAttachments).hasSize(1);

        Attachment attachment = partsAttachments.get("part1");
        Map<String, String> attributes = attachment.attributes();
        assertThat(attributes).containsEntry("filename", "my-picture.jpg");
    }
}
