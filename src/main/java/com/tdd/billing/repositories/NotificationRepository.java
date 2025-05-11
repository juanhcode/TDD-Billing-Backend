package com.tdd.billing.repositories;

import com.tdd.billing.entities.Notification;
import com.tdd.billing.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
}
