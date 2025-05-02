package com.tdd.billing.services;

import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Supplier;
import com.tdd.billing.repositories.ProductRepository;
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

    // Obtener un producto por nombre
    public Optional<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    // Listar todos los productos activos
    public List<Product> listActiveProducts() {
        return productRepository.findByStatusTrue();
    }

    // Listar productos por tienda
    public List<Product> listProductsByStore(Store store) {
        return productRepository.findByStore(store);
    }

    // Listar productos activos por tienda
    public List<Product> listActiveProductsByStore(Store store) {
        return productRepository.findByStoreAndStatusTrue(store);
    }

    // Listar productos por categoría
    public List<Product> listProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    // Listar productos por proveedor
    public List<Product> listProductsBySupplier(Supplier supplier) {
        return productRepository.findBySupplier(supplier);
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
        product.setSupplier(productDetails.getSupplier());

        return productRepository.save(product);
    }

    // Eliminar producto
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
