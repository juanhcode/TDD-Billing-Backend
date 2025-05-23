package com.tdd.billing.repositories;

import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Buscar categoría por nombre
    Optional<Category> findByName(String name);

    // Listar todas las categorías activas
    List<Category> findByStatusTrue();

    // Buscar categorías por tienda
    Page<Category> findByStore(Store store, Pageable pageable);


    // Buscar categorías activas en una tienda específica
    List<Category> findByStoreAndStatusTrue(Store store);
}
