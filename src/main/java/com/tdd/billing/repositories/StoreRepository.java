package com.tdd.billing.repositories;

import com.tdd.billing.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // Búsqueda por nombre exacto
    Optional<Store> findByName(String name);

    // Búsqueda de tiendas activas
    List<Store> findByStatusTrue();

    // Búsqueda por dirección que contenga texto
    List<Store> findByAddressContaining(String address);
}