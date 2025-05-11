package com.tdd.billing.services;

import com.tdd.billing.dto.CategoryResponseDTO;
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

    public Category registerCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> listActiveCategories() {
        return categoryRepository.findByStatusTrue();
    }


    public List<CategoryResponseDTO> listCategoriesByStoreDTO(Store store) {
        return categoryRepository.findByStore(store).stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<CategoryResponseDTO> listActiveCategoriesByStoreDTO(Store store) {
        return categoryRepository.findByStoreAndStatusTrue(store).stream()
                .map(this::mapToDTO)
                .toList();
    }
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setStatus(categoryDetails.getStatus());
        category.setStore(categoryDetails.getStore());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


    private CategoryResponseDTO mapToDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getStatus(),
                category.getCreatedAt()
        );
    }
}

