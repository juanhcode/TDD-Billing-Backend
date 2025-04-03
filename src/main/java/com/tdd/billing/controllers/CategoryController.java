package com.tdd.billing.controllers;

import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Product;
import com.tdd.billing.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.crearCategoria(category));
    }

    @GetMapping
    public ResponseEntity<List<Category>> listActiveCategories() {
        return ResponseEntity.ok(categoryService.listarCategoriasActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.buscarPorId(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Category>> getCategoriesByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(categoryService.buscarPorTienda(storeId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.buscarPorNombre(name));
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.obtenerProductosPorCategoria(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.actualizarCategoria(id, category));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Category> toggleCategoryStatus(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.cambiarEstado(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
