package com.tdd.billing.services;

import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

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
    void testCreateStore() {
        when(storeRepository.save(any(Store.class))).thenReturn(sampleStore);

        Store result = storeService.create(sampleStore);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Tienda Test");
        verify(storeRepository, times(1)).save(sampleStore);
    }

    @Test
    void testFindByIdWhenStoreExists() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(sampleStore));

        Optional<Store> result = storeService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Tienda Test");
        verify(storeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdWhenStoreDoesNotExist() {
        when(storeRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Store> result = storeService.findById(2L);

        assertThat(result).isNotPresent();
        verify(storeRepository, times(1)).findById(2L);
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
        verify(storeRepository, times(1)).save(updated);
    }
}
