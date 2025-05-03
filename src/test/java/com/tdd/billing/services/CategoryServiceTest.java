package com.tdd.billing.services;

import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.CategoryRepository;
import com.tdd.billing.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Store store;
    private Category category;

    @BeforeEach
    void setUp() {
        store = new Store();
        store.setId(1L);
        store.setName("Tienda Central");
        store.setDescription("Sucursal principal");
        store.setStatus(true);
        store.setCreatedAt(LocalDateTime.now());
        store.setAddress("Dirección Central");

        category = new Category();
        category.setId(1L);
        category.setStore(store);
        category.setName("Electrónicos");
        category.setDescription("Productos tecnológicos");
        category.setStatus(true);
        category.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testRegisterCategory() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category saved = categoryService.registerCategory(category);

        assertNotNull(saved);
        assertEquals("Electrónicos", saved.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> found = categoryService.getCategoryById(1L);

        assertTrue(found.isPresent());
        assertEquals("Electrónicos", found.get().getName());
    }

    @Test
    void testGetCategoryByName() {
        when(categoryRepository.findByName("Electrónicos")).thenReturn(Optional.of(category));

        Optional<Category> found = categoryService.getCategoryByName("Electrónicos");

        assertTrue(found.isPresent());
        assertEquals("Electrónicos", found.get().getName());
    }

    @Test
    void testListActiveCategories() {
        when(categoryRepository.findByStatusTrue()).thenReturn(List.of(category));

        List<Category> categories = categoryService.listActiveCategories();

        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
    }

    @Test
    void testListCategoriesByStore() {
        when(categoryRepository.findByStore(store)).thenReturn(List.of(category));

        List<Category> categories = categoryService.listCategoriesByStore(store);

        assertEquals(1, categories.size());
        assertEquals(store, categories.get(0).getStore());
    }

    @Test
    void testListActiveCategoriesByStore() {
        when(categoryRepository.findByStoreAndStatusTrue(store)).thenReturn(List.of(category));

        List<Category> categories = categoryService.listActiveCategoriesByStore(store);

        assertEquals(1, categories.size());
        assertTrue(categories.get(0).getStatus());
    }

    @Test
    void testUpdateCategory() {
        Category updatedDetails = new Category();
        updatedDetails.setName("Actualizado");
        updatedDetails.setDescription("Categoría actualizada");
        updatedDetails.setStatus(false);
        updatedDetails.setStore(store);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedDetails);

        Category updatedCategory = categoryService.updateCategory(1L, updatedDetails);

        assertNotNull(updatedCategory);
        assertEquals("Actualizado", updatedCategory.getName());
        assertFalse(updatedCategory.getStatus());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}

