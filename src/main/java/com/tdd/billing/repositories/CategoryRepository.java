package com.tdd.billing.repositories;

import com.tdd.billing.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Búsqueda por nombre exacto (equivalente a findByEmail)
    Optional<Category> findByName(String name);

    // Búsqueda de categorías activas
    List<Category> findByStatusTrue();

    // Búsqueda por nombre que contenga texto (insensible a mayúsculas)
    List<Category> findByNameContainingIgnoreCase(String name);
}