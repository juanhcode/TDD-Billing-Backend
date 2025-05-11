package com.tdd.billing.services;

import com.tdd.billing.dto.NotificationRequestDTO;
import com.tdd.billing.dto.NotificationResponseDTO;
import com.tdd.billing.entities.Notification;
import com.tdd.billing.entities.Product;
import com.tdd.billing.repositories.NotificationRepository;
import com.tdd.billing.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private NotificationService notificationService;

    private NotificationRequestDTO requestDTO;
    private Product sampleProduct;
    private Notification savedNotification;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(10L);

        requestDTO = new NotificationRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setProductId(10L);
        requestDTO.setTitle("Nueva oferta");
        requestDTO.setMessage("Hay una oferta especial para ti");
        requestDTO.setType("INFO");

        savedNotification = new Notification();
        savedNotification.setId(100L);
        savedNotification.setUserId(1L);
        savedNotification.setProduct(sampleProduct);
        savedNotification.setTitle("Nueva oferta");
        savedNotification.setMessage("Hay una oferta especial para ti");
        savedNotification.setType("INFO");
        savedNotification.setIsRead(false);
        savedNotification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateNotificationSuccessfully() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        NotificationResponseDTO result = notificationService.crearNotificacionDTO(requestDTO);

        assertNotNull(result);
        assertEquals(savedNotification.getId(), result.getId());
        assertEquals("Nueva oferta", result.getTitle());
        assertFalse(result.getIsRead());
    }

    @Test
    void shouldThrowExceptionIfProductNotFound() {
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> notificationService.crearNotificacionDTO(requestDTO));
    }

    @Test
    void shouldListNotificationsByUserId() {
        when(notificationRepository.findByUserId(1L)).thenReturn(List.of(savedNotification));

        List<NotificationResponseDTO> result = notificationService.listarPorUsuario(1L);

        assertEquals(1, result.size());
        assertEquals(savedNotification.getId(), result.get(0).getId());
    }

    @Test
    void shouldReturnEmptyListIfUserHasNoNotifications() {
        when(notificationRepository.findByUserId(2L)).thenReturn(Collections.emptyList());

        List<NotificationResponseDTO> result = notificationService.listarPorUsuario(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenProductNotFound() {
        requestDTO.setProductId(999L); // Producto no existente

        // Simula que el producto no se encuentra
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Verifica que la excepción sea lanzada
        assertThrows(EntityNotFoundException.class, () -> {
            notificationService.crearNotificacionDTO(requestDTO);
        });
    }


    @Test
    void shouldThrowRuntimeExceptionOnSaveFailure() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(notificationRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                notificationService.crearNotificacionDTO(requestDTO));

        assertEquals("DB error", ex.getMessage());
    }

    @Test
    void shouldHandleProductWithoutIdGracefully() {
        Product invalidProduct = new Product(); // ID null
        when(productRepository.findById(10L)).thenReturn(Optional.of(invalidProduct));

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(102L);
            return n;
        });

        NotificationResponseDTO result = notificationService.crearNotificacionDTO(requestDTO);

        assertNull(result.getProductId());
    }

    @Test
    void shouldInitializeIsReadToFalse() {
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));

        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        when(notificationRepository.save(captor.capture())).thenReturn(savedNotification);

        notificationService.crearNotificacionDTO(requestDTO);

        Notification saved = captor.getValue();
        assertFalse(saved.getIsRead());
    }
}
