package com.tdd.billing.repositories;

import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Búsqueda por nombre exacto
    Optional<Category> findByName(String name);

    // Búsqueda de categorías activas
    List<Category> findByStatusTrue();

    // Búsqueda por nombre que contenga texto (insensible a mayúsculas)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Búsqueda por tienda
    List<Category> findByStore(Store store);

    // Método para obtener productos por categoría
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoriaId")
    List<Product> findProductsByCategoryId(@Param("categoriaId") Long categoriaId);


    // Eliminar por ID con verificación de productos
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :id AND NOT EXISTS (SELECT 1 FROM Product p WHERE p.category.id = :id)")
    int deleteByIdIfNoProducts(@Param("id") Long id);

}