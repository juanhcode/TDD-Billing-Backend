package com.tdd.billing.repositories;


import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Equivalente a findByEmail para productos (búsqueda por nombre exacto)
    Optional<Product> findByName(String name);

    //Usando el nombre del campo (recomendado)
    List<Product> findByCategory(Category category);

    List<Product> findByStore(Store store);

    // Búsqueda de productos activos (como en UserRepository)
    List<Product> findByStatusTrue();

    // Búsqueda por categoríaID
    List<Product> findByCategoryId(Long categoryId);

    // Búsqueda por tienda (store)
    List<Product> findByStoreId(Long storeId);

    // Búsqueda por nombre que contenga un texto (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String nameFragment);
}


