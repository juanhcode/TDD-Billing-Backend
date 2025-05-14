package com.tdd.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.services.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private StoreController storeController;

    private ObjectMapper objectMapper;

    private Store sampleStore;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
        objectMapper = new ObjectMapper().findAndRegisterModules();

        User user = new User();
        user.setId(1L);

        sampleStore = new Store();
        sampleStore.setId(1L);
        sampleStore.setName("Test Store");
        sampleStore.setUrl("test-url");
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
    void testRegisterStoreSuccess() throws Exception {
        when(storeService.create(any(Store.class))).thenReturn(sampleStore);

        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Store"));

        verify(storeService).create(any(Store.class));
    }

    @Test
    void testRegisterStoreFailure() throws Exception {
        when(storeService.create(any(Store.class))).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error creating store: DB error"));
    }

    @Test
    void testUpdateStoreSuccess() throws Exception {
        Store updatedStore = new Store();
        updatedStore.setId(1L);
        updatedStore.setName("Updated Store");

        when(storeService.findById(1L)).thenReturn(Optional.of(sampleStore));
        when(storeService.update(any(Store.class))).thenReturn(updatedStore);

        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStore)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Store"));

        verify(storeService).findById(1L);
        verify(storeService).update(any(Store.class));
    }

    @Test
    void testUpdateStoreNotFound() throws Exception {
        when(storeService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Store not found"));

        verify(storeService).findById(1L);
        verify(storeService, never()).update(any(Store.class));
    }

    @Test
    void testUpdateStoreFailure() throws Exception {
        when(storeService.findById(1L)).thenReturn(Optional.of(sampleStore));
        when(storeService.update(any(Store.class))).thenThrow(new RuntimeException("DB update error"));

        mockMvc.perform(put("/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStore)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error updating store: DB update error"));
    }
}
