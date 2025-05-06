package com.tdd.billing.repositories;


import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Buscar producto por nombre (opcional: sensible o no a mayúsculas)
    Optional<Product> findByName(String name);

    // Listar todos los productos activos
    List<Product> findByStatusTrue();

    // Buscar todos los productos de una tienda específica
    List<Product> findByStore(Store store);

    // Buscar productos por categoría
    List<Product> findByCategory(Category category);


    // Buscar productos activos en una tienda específica
    List<Product> findByStoreAndStatusTrue(Store store);
}



