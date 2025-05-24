package com.tdd.billing.repositories;


import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar producto por nombre (opcional: sensible o no a mayúsculas)
    Optional<Product> findByName(String name);

    // Listar todos los productos activos
    List<Product> findByStatusTrue();

    // Buscar todos los productos de una tienda específica
    Page<Product> findByStore(Store store, Pageable pageable);


    Page<Product> findByStoreAndCategoryAndStatusTrue(Store store, Category category, Pageable pageable);



    // Buscar productos por categoría
    List<Product> findByCategory(Category category);


    // Buscar productos activos en una tienda específica
    Page<Product> findByStoreAndStatusTrue(Store store, Pageable pageable);
}



