package com.tdd.billing.services;

import com.tdd.billing.dto.ProductResponseDTO;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import com.tdd.billing.repositories.ProductRepository;
import com.tdd.billing.utils.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Registrar un nuevo producto
    public Product registerProduct(Product product) {
        return productRepository.save(product);
    }

    // Obtener un producto por ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    // Listar todos los productos activos
    public List<Product> listActiveProducts() {
        return productRepository.findByStatusTrue();
    }

    // Listar productos por tienda
    public List<ProductResponseDTO> listProductsByStore(Long storeId) {
        Store store = new Store();
        store.setId(storeId);

        return productRepository.findByStore(store)
                .stream()
                .map(ProductMapper::toResponseDTO)
                .toList();
    }


    // Listar productos activos por tienda
    public List<Product> listActiveProductsByStore(Store store) {
        return productRepository.findByStoreAndStatusTrue(store);
    }

    // Listar productos por categoría
    public List<Product> listProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }



    // Actualizar un producto existente
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setStatus(productDetails.getStatus());
        product.setStore(productDetails.getStore());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus(false);
        productRepository.save(product);
    }

    public ProductResponseDTO convertToProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setUrl(product.getUrl());
        dto.setRatingRate(product.getRatingRate());
        dto.setRatingCount(product.getRatingCount());
        dto.setStatus(product.getStatus());
        return dto;
    }
}
