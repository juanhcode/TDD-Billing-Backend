package com.tdd.billing.controllers;

import com.tdd.billing.dto.CategoryDTO;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category c = new Category();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());

        Store store = new Store();
        store.setId(dto.getStoreId());
        c.setStore(store);

        c.setCreatedAt(dto.getCreatedAt());
        return c;
    }

    private CategoryDTO convertToDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setStatus(c.getStatus());
        dto.setStoreId(c.getStore().getId());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> register(@RequestBody CategoryDTO dto) {
        Category saved = categoryService.registerCategory(convertToEntity(dto));
        return ResponseEntity.ok(convertToDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> listActiveCategories() {
        List<CategoryDTO> dtos = categoryService.listActiveCategories()
                .stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(c -> ResponseEntity.ok(convertToDTO(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDTO> getByName(@PathVariable String name) {
        Optional<Category> category = categoryService.getCategoryByName(name);
        return category.map(c -> ResponseEntity.ok(convertToDTO(c)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDTO>> getByStore(@PathVariable Long storeId) {
        Store store = new Store();
        store.setId(storeId);
        List<CategoryDTO> dtos = categoryService.listCategoriesByStore(store)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<CategoryDTO>> getActiveByStore(@PathVariable Long storeId) {
        Store store = new Store();
        store.setId(storeId);
        List<CategoryDTO> dtos = categoryService.listActiveCategoriesByStore(store)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        Category updated = categoryService.updateCategory(id, convertToEntity(dto));
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

