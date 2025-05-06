package com.tdd.billing.services;

import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Registrar una nueva categoría
    public Category registerCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Obtener una categoría por ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Obtener una categoría por nombre
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    // Listar todas las categorías activas
    public List<Category> listActiveCategories() {
        return categoryRepository.findByStatusTrue();
    }

    // Listar categorías por tienda
    public List<Category> listCategoriesByStore(Store store) {
        return categoryRepository.findByStore(store);
    }

    // Listar categorías activas por tienda
    public List<Category> listActiveCategoriesByStore(Store store) {
        return categoryRepository.findByStoreAndStatusTrue(store);
    }

    // Actualizar una categoría existente
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setStatus(categoryDetails.getStatus());
        category.setStore(categoryDetails.getStore());

        return categoryRepository.save(category);
    }

    // Eliminar una categoría
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

