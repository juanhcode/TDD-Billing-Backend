package com.tdd.billing.controllers;

import com.tdd.billing.dto.ProductDTO;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import com.tdd.billing.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setStock(dto.getStock());
        p.setStatus(dto.getStatus());

        Store s = new Store(); s.setId(dto.getStoreId()); p.setStore(s);
        Category c = new Category(); c.setId(dto.getCategoryId()); p.setCategory(c);

        p.setCreatedAt(dto.getCreatedAt());
        return p;
    }

    private ProductDTO convertToDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setStatus(p.getStatus());
        dto.setStoreId(p.getStore().getId());
        dto.setCategoryId(p.getCategory().getId());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> register(@RequestBody ProductDTO dto) {
        Product saved = productService.registerProduct(convertToEntity(dto));
        return ResponseEntity.ok(convertToDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> listActiveProducts() {
        List<ProductDTO> dtos = productService.listActiveProducts()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Long id) {
        Optional<Product> op = productService.getProductById(id);
        return op.map(p -> ResponseEntity.ok(convertToDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDTO> getByName(@PathVariable String name) {
        Optional<Product> op = productService.getProductByName(name);
        return op.map(p -> ResponseEntity.ok(convertToDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductDTO>> getByStore(@PathVariable Long storeId) {
        Store s = new Store(); s.setId(storeId);
        List<ProductDTO> dtos = productService.listProductsByStore(s)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<ProductDTO>> getActiveByStore(@PathVariable Long storeId) {
        Store s = new Store(); s.setId(storeId);
        List<ProductDTO> dtos = productService.listActiveProductsByStore(s)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getByCategory(@PathVariable Long categoryId) {
        Category c = new Category(); c.setId(categoryId);
        List<ProductDTO> dtos = productService.listProductsByCategory(c)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        Product updated = productService.updateProduct(id, convertToEntity(dto));
        return ResponseEntity.ok(convertToDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}




