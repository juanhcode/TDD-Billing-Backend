package com.tdd.billing.services;

import com.tdd.billing.entities.Notification;
import com.tdd.billing.entities.Product;
import com.tdd.billing.repositories.NotificationRepository;
import com.tdd.billing.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ProductRepository productRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               ProductRepository productRepository) {
        this.notificationRepository = notificationRepository;
        this.productRepository = productRepository;
    }

    // Crear una nueva notificación
    @Transactional
    public Notification crearNotificacion(Long productId, String title, String message) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));


        Notification notificacion = new Notification();
        notificacion.setProduct(product);
        notificacion.setTitle(title);
        notificacion.setMessage(message);
        notificacion.setRead(false);
        notificacion.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notificacion);
    }

    // Listar todas las notificaciones
    public List<Notification> listarTodas() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    // Listar notificaciones no leídas
    public List<Notification> listarNoLeidas() {
        return notificationRepository.findByIsReadFalse();
    }

    // Listar notificaciones por producto
    public List<Notification> listarPorProducto(Long productId) {
        return notificationRepository.findByProductId(productId);
    }

    // Marcar una notificación como leída
    @Transactional
    public Notification marcarComoLeida(Long notificationId) {
        Notification notificacion = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada"));

        notificacion.setRead(true);
        return notificationRepository.save(notificacion);
    }

    // Eliminar una notificación
    @Transactional
    public void eliminarNotificacion(Long id) {
        notificationRepository.deleteById(id);
    }
}

