package de.codecentric.reedelk.rest.internal.client.strategy;

import de.codecentric.reedelk.rest.internal.client.body.BodyResult;
import de.codecentric.reedelk.runtime.api.message.content.Attachment;
import de.codecentric.reedelk.runtime.api.message.content.MimeType;
import org.apache.http.HttpEntity;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpEntityBuilderTest {

    @Mock
    private BodyResult bodyResult;

    @Test
    void shouldBuildByteArrayEntityCorrectly() {
        // Given
        byte[] contentAsBytes = "Test content body".getBytes();

        doReturn(false)
                .when(bodyResult)
                .isMultipart();

        doReturn(contentAsBytes)
                .when(bodyResult)
                .getDataAsBytes();

        // When
        HttpEntity actual = HttpEntityBuilder.get()
                .bodyProvider(bodyResult)
                .build();

        // Then
        assertThat(actual).isInstanceOf(NByteArrayEntity.class);
        assertThat(actual.getContentLength()).isEqualTo(contentAsBytes.length);

        verify(bodyResult, never()).getDataAsMultipart();
    }

    @Test
    void shouldBuildMultipartEntityCorrectly() {
        // Given
        Attachment myPicturePart = Attachment.builder()
                .attribute("filename", "my_picture.jpg")
                .data("picturebytes".getBytes())
                .mimeType(MimeType.IMAGE_JPEG)
                .build();

        Attachment myFilePart = Attachment.builder()
                .attribute("filename", "myFile.wav")
                .mimeType(MimeType.APPLICATION_BINARY)
                .data("filebytes".getBytes())
                .build();

        Map<String,Attachment> parts = new HashMap<>();
        parts.put("myPicturePart", myPicturePart);
        parts.put("myFilePart", myFilePart);

        doReturn(true)
                .when(bodyResult)
                .isMultipart();

        doReturn(parts)
                .when(bodyResult)
                .getDataAsMultipart();

        // When
        HttpEntity actual = HttpEntityBuilder.get()
                .bodyProvider(bodyResult)
                .build();

        // Then
        assertThat(actual).isInstanceOf(MultipartFormEntityWrapper.class);
        verify(bodyResult, never()).getDataAsBytes();
    }
}
