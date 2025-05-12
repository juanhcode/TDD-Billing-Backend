package com.tdd.billing.controllers;

import com.tdd.billing.dto.ProductResponseDTO;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import com.tdd.billing.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> register(@RequestBody Product product) {
        Product saved = productService.registerProduct(product);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Product>> listActiveProducts() {
        List<Product> products = productService.listActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductResponseDTO>> getByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(productService.listProductsByStore(storeId));
    }


    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<Product>> getActiveByStore(@PathVariable Long storeId) {
        Store store = new Store();
        store.setId(storeId);
        List<Product> products = productService.listActiveProductsByStore(store);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        List<Product> products = productService.listProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
