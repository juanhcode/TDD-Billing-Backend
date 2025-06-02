package com.tdd.billing.services;
import com.tdd.billing.dto.StoreRequestDTO;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private StoreService storeService;

    private Store sampleStore;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);

        sampleStore = new Store();
        sampleStore.setId(1L);
        sampleStore.setName("Tienda Test");
        sampleStore.setUrl("tienda-test");
        sampleStore.setEmail("test@example.com");
        sampleStore.setContact("123456789");
        sampleStore.setNit("1234567890");
        sampleStore.setLogo("logo.png");
        sampleStore.setDescription("Tienda de prueba");
        sampleStore.setStatus(true);
        sampleStore.setAddress("Calle Falsa 123");
        sampleStore.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateStoreThrowsIOException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(s3Service.uploadFile(file)).thenThrow(new IOException("upload failed"));

        StoreRequestDTO dto = new StoreRequestDTO();
        dto.setName("Tienda Test");

        assertThatThrownBy(() -> storeService.create(dto, file))
                .isInstanceOf(IOException.class)
                .hasMessage("upload failed");
    }

    @Test
    void testFindByIdWhenStoreExists() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(sampleStore));

        Optional<Store> result = storeService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Tienda Test");
        verify(storeRepository).findById(1L);
    }

    @Test
    void testFindByIdWhenStoreDoesNotExist() {
        when(storeRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Store> result = storeService.findById(2L);

        assertThat(result).isNotPresent();
        verify(storeRepository).findById(2L);
    }

    @Test
    void testUpdateStore() {
        Store updated = new Store();
        updated.setId(1L);
        updated.setName("Tienda Actualizada");

        when(storeRepository.save(any(Store.class))).thenReturn(updated);

        Store result = storeService.update(updated);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Tienda Actualizada");
        verify(storeRepository).save(updated);
    }

    @Test
    void testGetStoreDTOByIdWhenExists() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(sampleStore));

        Optional<StoreResponseDTO> result = storeService.getStoreDTOById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Tienda Test");
        verify(storeRepository).findById(1L);
    }

    @Test
    void testGetStoreDTOByIdWhenNotExists() {
        when(storeRepository.findById(10L)).thenReturn(Optional.empty());

        Optional<StoreResponseDTO> result = storeService.getStoreDTOById(10L);

        assertThat(result).isNotPresent();
        verify(storeRepository).findById(10L);
    }
}
