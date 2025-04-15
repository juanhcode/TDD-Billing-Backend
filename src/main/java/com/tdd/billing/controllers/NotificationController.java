package com.tdd.billing.controllers;

import com.tdd.billing.entities.Notification;
import com.tdd.billing.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Crear una nueva notificación para un producto
    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestParam Long productId,
            @RequestParam String title,
            @RequestParam String message
    ) {
        Notification notification = notificationService.crearNotificacion(productId, title, message);
        return ResponseEntity.ok(notification);
    }

    // Listar todas las notificaciones
    @GetMapping
    public ResponseEntity<List<Notification>> listAllNotifications() {
        return ResponseEntity.ok(notificationService.listarTodas());
    }

    // Listar notificaciones no leídas
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> listUnreadNotifications() {
        return ResponseEntity.ok(notificationService.listarNoLeidas());
    }

    // Listar notificaciones por producto
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Notification>> listByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(notificationService.listarPorProducto(productId));
    }

    // Marcar una notificación como leída
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Notification updated = notificationService.marcarComoLeida(id);
        return ResponseEntity.ok(updated);
    }

    // Eliminar una notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}

