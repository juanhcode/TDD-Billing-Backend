package com.tdd.billing.services;
import com.tdd.billing.dto.NotificationRequestDTO;
import com.tdd.billing.dto.NotificationResponseDTO;
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

    @Transactional
    public NotificationResponseDTO createNotificationDTO(NotificationRequestDTO request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setProduct(product);
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        Notification saved = notificationRepository.save(notification);

        return mapToResponseDTO(saved);
    }

    public List<NotificationResponseDTO> listByUser(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private NotificationResponseDTO mapToResponseDTO(Notification notification) {
        NotificationResponseDTO dto = new NotificationResponseDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setProductId(notification.getProduct() != null ? notification.getProduct().getId() : null);
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
