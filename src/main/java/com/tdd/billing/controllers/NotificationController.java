package com.tdd.billing.controllers;

import com.tdd.billing.dto.NotificationRequestDTO;
import com.tdd.billing.dto.NotificationResponseDTO;
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

    @PostMapping("/create")
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO request) {
        return ResponseEntity.ok(notificationService.crearNotificacionDTO(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.listarPorUsuario(userId));
    }
}

