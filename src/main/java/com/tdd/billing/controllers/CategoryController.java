package com.tdd.billing.controllers;
import com.tdd.billing.dto.CategoryDTO;
import com.tdd.billing.dto.CategoryResponseDTO;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final StoreRepository storeRepository;

    public CategoryController(CategoryService categoryService, StoreRepository storeRepository) {
        this.categoryService = categoryService;
        this.storeRepository = storeRepository;
    }

    @PostMapping
    public ResponseEntity<Category> register(@RequestBody Category category) {
        // Verificar si la tienda existe
        Store store = storeRepository.findById(category.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Asignar la tienda a la categoría
        category.setStore(store);

        // Guardar la categoría
        Category savedCategory = categoryService.registerCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<Page<CategoryResponseDTO>> getByStore(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CategoryResponseDTO> categories = categoryService.listCategoriesByStoreDTO(storeId, page, size);
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<CategoryResponseDTO>> getActiveByStore(@PathVariable Long storeId) {
        Store store = new Store();
        store.setId(storeId);
        List<CategoryResponseDTO> categories = categoryService.listActiveCategoriesByStoreDTO(store);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
