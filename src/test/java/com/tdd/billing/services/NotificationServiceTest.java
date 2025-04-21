package com.tdd.billing.services;

import com.tdd.billing.entities.Notification;
import com.tdd.billing.entities.Product;
import com.tdd.billing.repositories.NotificationRepository;
import com.tdd.billing.repositories.ProductRepository;
import com.tdd.billing.services.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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

    private Product product;
    private Notification notification;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Producto de prueba");

        notification = new Notification();
        notification.setId(1L);
        notification.setProduct(product);
        notification.setTitle("Título");
        notification.setMessage("Mensaje");
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void crearNotificacion_CuandoProductoExiste() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification resultado = notificationService.crearNotificacion(1L, "Título", "Mensaje");

        assertNotNull(resultado);
        assertEquals("Título", resultado.getTitle());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void crearNotificacion_CuandoProductoNoExiste() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> notificationService.crearNotificacion(1L, "Título", "Mensaje"));
    }

    @Test
    void listarTodas() {
        when(notificationRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(notification));

        List<Notification> resultado = notificationService.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals("Título", resultado.get(0).getTitle());
    }

    @Test
    void listarNoLeidas() {
        when(notificationRepository.findByIsReadFalse()).thenReturn(List.of(notification));

        List<Notification> resultado = notificationService.listarNoLeidas();

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).isRead());
    }

    @Test
    void listarPorProducto() {
        when(notificationRepository.findByProductId(1L)).thenReturn(List.of(notification));

        List<Notification> resultado = notificationService.listarPorProducto(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getProduct().getId());
    }

    @Test
    void marcarComoLeida_CuandoExiste() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Notification resultado = notificationService.marcarComoLeida(1L);

        assertTrue(resultado.isRead());
    }

    @Test
    void marcarComoLeida_CuandoNoExiste() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> notificationService.marcarComoLeida(1L));
    }

    @Test
    void eliminarNotificacion() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.eliminarNotificacion(1L);

        verify(notificationRepository, times(1)).deleteById(1L);
    }
}

