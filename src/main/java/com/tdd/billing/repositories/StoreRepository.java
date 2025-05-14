package com.tdd.billing.repositories;

import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);

    List<Store> findByStatusTrue();

    List<Store> findByAddressContaining(String address);
}