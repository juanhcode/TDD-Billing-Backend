package com.tdd.billing;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tdd.billing.services.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setup() {
        // Inyectar el valor del bucket manualmente
        s3Service = new S3Service(amazonS3);
        // Simular @Value
        var bucketNameField = "bucketName";
        try {
            var field = S3Service.class.getDeclaredField(bucketNameField);
            field.setAccessible(true);
            field.set(s3Service, "test-bucket");
        } catch (Exception e) {
            throw new RuntimeException("Error inyectando bucketName", e);
        }
    }

    @Test
    void testUploadFile_ReturnsValidUrl() throws IOException {
        // Arrange
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                "mock content".getBytes()
        );

        // Act
        String url = s3Service.uploadFile(multipartFile);

        // Assert
        assertThat(url).startsWith("https://test-bucket.s3.amazonaws.com/");
        assertThat(url).endsWith(".png");

        // Verifica que amazonS3.putObject fue llamado
        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(amazonS3, times(1)).putObject(captor.capture());

        PutObjectRequest actualRequest = captor.getValue();
        assertThat(actualRequest.getBucketName()).isEqualTo("test-bucket");
        assertThat(actualRequest.getKey()).endsWith(".png");
    }
}
