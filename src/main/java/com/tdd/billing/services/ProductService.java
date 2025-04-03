package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository,
                          StoreRepository storeRepository,
                          CategoryRepository categoryRepository,
                          SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public Product crearProducto(Product producto) {
        // Validar que las relaciones existan
        Store store = storeRepository.findById(producto.getStore().getId())
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
        Category category = categoryRepository.findById(producto.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Supplier supplier = supplierRepository.findById(producto.getSupplier().getId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        // Asignar las entidades completas al producto
        producto.setStore(store);
        producto.setCategory(category);
        producto.setSupplier(supplier);

        return productRepository.save(producto);
    }

    public List<Product> listarProductosActivos() {
        return productRepository.findByStatusTrue();
    }

    public Optional<Product> buscarPorId(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product actualizarProducto(Long id, Product cambios) {
        Product producto = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizar campos básicos
        producto.setName(cambios.getName());
        producto.setDescription(cambios.getDescription());
        producto.setPrice(cambios.getPrice());
        producto.setStock(cambios.getStock());
        producto.setStatus(cambios.isStatus());

        // Actualizar relaciones si se proporcionan
        if (cambios.getStore() != null) {
            Store store = storeRepository.findById(cambios.getStore().getId())
                    .orElseThrow(() -> new RuntimeException("Tienda no encontrada"));
            producto.setStore(store);
        }

        if (cambios.getCategory() != null) {
            Category category = categoryRepository.findById(cambios.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategory(category);
        }

        if (cambios.getSupplier() != null) {
            Supplier supplier = supplierRepository.findById(cambios.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
            producto.setSupplier(supplier);
        }

        return productRepository.save(producto);
    }

    @Transactional
    public Product cambiarEstado(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        // Invierte el estado actual
        product.setStatus(!product.isStatus());

        return productRepository.save(product);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        productRepository.deleteById(id);
    }

    // Métodos adicionales útiles
    public List<Product> buscarPorNombre(String nombre) {
        return productRepository.findByNameContainingIgnoreCase(nombre);
    }

    public List<Product> buscarPorCategoria(Long categoriaId) {
        Category categoria = categoryRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        return productRepository.findByCategory(categoria);
    }

    public List<Product> buscarPorTienda(Long tiendaId) {
        Store tienda = storeRepository.findById(tiendaId)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));
        return productRepository.findByStore(tienda);
    }
}