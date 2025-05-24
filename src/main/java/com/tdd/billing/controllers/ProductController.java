package com.tdd.billing.controllers;
import com.tdd.billing.dto.ProductRequestDTO;
import com.tdd.billing.dto.ProductResponseDTO;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import com.tdd.billing.services.ProductService;
import com.tdd.billing.services.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final S3Service s3Service;

    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> register(@RequestPart("product") ProductRequestDTO product,
                                                       @RequestPart("file") MultipartFile file) throws IOException {
        String photoUrl = s3Service.uploadFile(file);
        ProductResponseDTO saved = productService.registerProduct(product, photoUrl);
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

    @GetMapping("/store/{storeId}/{categoryId}")
    public ResponseEntity<Page<ProductResponseDTO>> getByStore(
            @PathVariable Long storeId,
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponseDTO> products = productService.listProductsByStore(storeId, categoryId, page, size);
        return ResponseEntity.ok(products);
    }




//    @GetMapping("/store/{storeId}/active")
//    public ResponseEntity<List<Product>> getActiveByStore(@PathVariable Long storeId) {
//        Store store = new Store();
//        store.setId(storeId);
//        List<Product> products = productService.listActiveProductsByStore(store);
//        return ResponseEntity.ok(products);
//    }

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

    @GetMapping("/random/{n}")
    public ResponseEntity<List<ProductResponseDTO>> getRandomProducts(@PathVariable("n") Long quantity) {
        List<ProductResponseDTO> products = productService.getRandomProducts(quantity);
        return ResponseEntity.ok(products);
    }
}
