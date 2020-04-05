package com.reedelk.rest.internal.client.body;

import com.reedelk.runtime.api.commons.ModuleContext;
import com.reedelk.runtime.api.converter.ConverterService;
import com.reedelk.runtime.api.flow.FlowContext;
import com.reedelk.runtime.api.message.Message;
import com.reedelk.runtime.api.message.MessageBuilder;
import com.reedelk.runtime.api.message.content.Attachment;
import com.reedelk.runtime.api.message.content.ByteArrayContent;
import com.reedelk.runtime.api.message.content.MimeType;
import com.reedelk.runtime.api.script.ScriptEngineService;
import com.reedelk.runtime.api.script.dynamicvalue.DynamicObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultBodyProviderTest {

    private ModuleContext context = new ModuleContext(10L);

    @Mock
    private ScriptEngineService scriptEngine;
    @Mock
    private ConverterService converterService;
    @Mock
    private FlowContext flowContext;

    @Test
    void shouldReturnIsMultipartFalseAndCorrectData() {
        // Given
        DynamicObject body = DynamicObject.from("#['My content']", context);
        DefaultBodyProvider bodyProvider = new DefaultBodyProvider(scriptEngine, converterService, body);

        Message message = MessageBuilder.get().empty().build();

        String evaluationResult = "My content";
        doReturn(Optional.of(evaluationResult))
                .when(scriptEngine)
                .evaluate(body, flowContext, message);

        byte[] dataAsBytes = "My content".getBytes();
        doReturn(dataAsBytes)
                .when(converterService)
                .convert(evaluationResult, byte[].class);

        // When
        BodyResult bodyResult = bodyProvider.get(message, flowContext);

        // Then
        assertThat(bodyResult.isMultipart()).isFalse();
        assertThat(bodyResult.getDataAsBytes()).isEqualTo(dataAsBytes);
    }

    @Test
    void shouldReturnIsMultipartTrueAndCorrectData() {
        // Given
        DynamicObject body = DynamicObject.from("#[message.payload()]", context);
        DefaultBodyProvider bodyProvider = new DefaultBodyProvider(scriptEngine, converterService, body);

        Map<String,Attachment> parts = createParts();

        Message message = MessageBuilder.get().withJavaObject(parts).build();

        doReturn(Optional.of(parts))
                .when(scriptEngine)
                .evaluate(body, flowContext, message);

        // When
        BodyResult bodyResult = bodyProvider.get(message, flowContext);

        // Then
        verify(converterService, never()).convert(any(), any(Class.class));

        assertThat(bodyResult.isMultipart()).isTrue();
        assertThat(bodyResult.getDataAsMultipart()).isEqualTo(parts);
    }

    @Test
    void shouldReturnIsStreamableTrue() {
        // Given
        DynamicObject body = DynamicObject.from("#[message.payload()]", context);
        DefaultBodyProvider bodyProvider = new DefaultBodyProvider(scriptEngine, converterService, body);

        Message message = MessageBuilder.get()
                .withString(Flux.just("one", "two", "three"), MimeType.TEXT_PLAIN)
                .build();

        // When
        boolean streamable = bodyProvider.streamable(message);

        // Then
        assertThat(streamable).isTrue();
    }

    @Test
    void shouldReturnIsStreamableFalse() {
        // Given
        DynamicObject body = DynamicObject.from("#[message.payload()]", context);
        DefaultBodyProvider bodyProvider = new DefaultBodyProvider(scriptEngine, converterService, body);

        Message message = MessageBuilder.get()
                .withJavaObject(Mono.just(createParts()))
                .build();

        // When
        boolean streamable = bodyProvider.streamable(message);

        // Then
        assertThat(streamable).isFalse();
    }

    @Test
    void shouldReturnIsStreamableFalseWhenBinaryBody() {
        // Given
        DynamicObject body = DynamicObject.from("#[context.myBinaryArray]", context);
        DefaultBodyProvider bodyProvider = new DefaultBodyProvider(scriptEngine, converterService, body);

        Message message = MessageBuilder.get()
                .withBinary("my binary data".getBytes())
                .build();

        // When
        boolean streamable = bodyProvider.streamable(message);

        // Then
        assertThat(streamable).isFalse();
    }

    private Map<String,Attachment> createParts() {
        ByteArrayContent pictureContent = new ByteArrayContent("picturebytes".getBytes(), MimeType.IMAGE_JPEG);
        Attachment myPicturePart = Attachment.builder()
                .attribute("filename", "my_picture.jpg")
                .content(pictureContent)
                .build();

        ByteArrayContent fileContent = new ByteArrayContent("filebytes".getBytes(), MimeType.APPLICATION_BINARY);
        Attachment myFilePart = Attachment.builder()
                .attribute("filename", "myFile.wav")
                .content(fileContent)
                .build();

        Map<String,Attachment> parts = new HashMap<>();
        parts.put("myPicturePart", myPicturePart);
        parts.put("myFilePart", myFilePart);
        return parts;
    }
}
