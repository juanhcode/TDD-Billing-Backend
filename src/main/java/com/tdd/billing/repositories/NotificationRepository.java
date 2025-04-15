package com.tdd.billing.repositories;

import com.tdd.billing.entities.Notification;
import com.tdd.billing.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Buscar notificaciones por producto
    List<Notification> findByProduct(Product product);

    // Buscar notificaciones no leídas
    List<Notification> findByIsReadFalse();

    // Buscar notificaciones por ID de producto
    List<Notification> findByProductId(Long productId);

    // Buscar notificaciones ordenadas por fecha de creación descendente
    List<Notification> findAllByOrderByCreatedAtDesc();
}
