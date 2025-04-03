package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           StoreRepository storeRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Category crearCategoria(Category categoria) {
        Store store = storeRepository.findById(categoria.getStore().getId())
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));

        categoria.setStore(store);
        return categoryRepository.save(categoria);
    }

    public List<Category> listarCategoriasActivas() {
        return categoryRepository.findByStatusTrue();
    }

    public Optional<Category> buscarPorId(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category actualizarCategoria(Long id, Category cambios) {
        Category categoria = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        // Actualizar campos básicos
        categoria.setName(cambios.getName());
        categoria.setDescription(cambios.getDescription());
        categoria.setStatus(cambios.isStatus());

        // Actualizar relación con tienda si se proporciona
        if (cambios.getStore() != null) {
            Store store = storeRepository.findById(cambios.getStore().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));
            categoria.setStore(store);
        }

        return categoryRepository.save(categoria);
    }

    @Transactional
    public Category cambiarEstado(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        // Invierte el estado actual
        category.setStatus(!category.isStatus());

        return categoryRepository.save(category);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        int deleted = categoryRepository.deleteByIdIfNoProducts(id);
        if (deleted == 0) {
            throw new IllegalStateException("No se puede eliminar: La categoría tiene productos asociados o no existe");
        }
    }

    // Métodos adicionales útiles
    public List<Category> buscarPorNombre(String nombre) {
        return categoryRepository.findByNameContainingIgnoreCase(nombre);
    }

    public List<Category> buscarPorTienda(Long tiendaId) {
        Store tienda = storeRepository.findById(tiendaId)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));
        return categoryRepository.findByStore(tienda);
    }

    public List<Product> obtenerProductosPorCategoria(Long categoriaId) {
        if (!categoryRepository.existsById(categoriaId)) {
            throw new EntityNotFoundException("Categoría no encontrada");
        }
        return categoryRepository.findProductsByCategoryId(categoriaId);
    }
}
