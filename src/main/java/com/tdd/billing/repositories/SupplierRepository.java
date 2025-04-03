package com.tdd.billing.repositories;

import com.tdd.billing.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // Búsqueda por email exacto (como el findByEmail de User)
    Optional<Supplier> findByEmail(String email);

    // Búsqueda por nombre que contenga texto
    List<Supplier> findByNameContainingIgnoreCase(String name);

    // Búsqueda por teléfono
    Optional<Supplier> findByPhone(String phone);
}