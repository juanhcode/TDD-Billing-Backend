package com.tdd.billing.controllers;

import com.tdd.billing.dto.StoreRequestDTO;
import com.tdd.billing.dto.StoreResponseDTO;
import com.tdd.billing.entities.Store;
import com.tdd.billing.services.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController storeController;

    private Store sampleStore;
    private StoreRequestDTO storeRequestDTO;
    private StoreResponseDTO storeResponseDTO;

    @BeforeEach
    void setUp() {
        sampleStore = new Store();
        sampleStore.setId(1L);
        sampleStore.setName("Tienda Uno");

        storeRequestDTO = new StoreRequestDTO();
        // Set propiedades relevantes si las tiene

        storeResponseDTO = new StoreResponseDTO();
        storeResponseDTO.setId(1L);
        storeResponseDTO.setName("Tienda Uno");
    }

    // ---------- TEST: POST /api/stores (register) ----------

    @Test
    void register_shouldReturnOk_whenStoreIsCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "logo.png", "image/png", "fake-image-content".getBytes());

        when(storeService.create(eq(storeRequestDTO), eq(file))).thenReturn(sampleStore);

        ResponseEntity<StoreResponseDTO> response = storeController.register(storeRequestDTO, file);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void register_shouldThrowException_whenServiceFails() {
        try {
            when(storeService.create(eq(storeRequestDTO), any())).thenThrow(new RuntimeException("DB error"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            storeController.register(storeRequestDTO, null);
        });

        assertTrue(thrown.getMessage().contains("Error creating store"));
    }

    // ---------- TEST: PUT /api/stores/{id} (update) ----------

    @Test
    void update_shouldReturnUpdatedStore_whenExists() {
        when(storeService.findById(1L)).thenReturn(Optional.of(sampleStore));
        when(storeService.update(sampleStore)).thenReturn(sampleStore);

        ResponseEntity<?> response = storeController.update(1L, sampleStore);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Store);
        assertEquals(1L, ((Store) response.getBody()).getId());
    }

    @Test
    void update_shouldReturnNotFound_whenStoreDoesNotExist() {
        when(storeService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = storeController.update(1L, sampleStore);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Store not found", response.getBody());
    }

    @Test
    void update_shouldReturnInternalServerError_onException() {
        when(storeService.findById(1L)).thenThrow(new RuntimeException("unexpected"));

        ResponseEntity<?> response = storeController.update(1L, sampleStore);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error updating store"));
    }

    // ---------- TEST: GET /api/stores/{id} (getStore) ----------

    @Test
    void getStore_shouldReturnStoreDTO_whenExists() {
        when(storeService.getStoreDTOById(1L)).thenReturn(Optional.of(storeResponseDTO));

        ResponseEntity<Object> response = storeController.getStore(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof StoreResponseDTO);
        assertEquals(1L, ((StoreResponseDTO) response.getBody()).getId());
    }

    @Test
    void getStore_shouldReturnNotFound_whenMissing() {
        when(storeService.getStoreDTOById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = storeController.getStore(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Store not found", response.getBody());
    }

    @Test
    void getStore_shouldReturnInternalServerError_onException() {
        when(storeService.getStoreDTOById(1L)).thenThrow(new RuntimeException("boom"));

        ResponseEntity<Object> response = storeController.getStore(1L);

        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Error retrieving store"));
    }
}
