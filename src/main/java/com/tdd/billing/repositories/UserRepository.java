package com.tdd.billing.repositories;

import com.tdd.billing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByStatusTrue();
    List<User> findByStoreId(Long storeId);
}

