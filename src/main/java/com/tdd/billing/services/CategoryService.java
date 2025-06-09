package com.tdd.billing.services;

import com.tdd.billing.dto.CategoryDTO;
import com.tdd.billing.dto.CategoryResponseDTO;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.CategoryRepository;
import com.tdd.billing.repositories.StoreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    public CategoryService(CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }

    public Category registerCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }


    public Page<CategoryResponseDTO> listCategoriesByStoreDTO(Long storeId, int page, int size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Page<Category> categories = categoryRepository.findByStore(store, PageRequest.of(page, size));
        return categories.map(this::mapToDTO);
    }

    public List<CategoryResponseDTO> listActiveCategoriesByStoreDTO(Store store) {
        return categoryRepository.findByStoreAndStatusTrue(store).stream()
                .map(this::mapToDTO)
                .toList();
    }
    public CategoryResponseDTO updateCategory(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.getStatus());

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));
        category.setStore(store);

        Category updated = categoryRepository.save(category);

        return new CategoryResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getStatus(),
                updated.getCreatedAt()
        );
    }


    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setStatus(false);
        categoryRepository.save(category);
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

