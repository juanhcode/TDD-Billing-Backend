package com.tdd.billing.services;
import com.tdd.billing.dto.CategoryResponseDTO;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.repositories.CategoryRepository;
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

    private Category sampleCategory;
    private Store sampleStore;

    @BeforeEach
    void setUp() {
        sampleStore = new Store();
        sampleStore.setId(1L);

        sampleCategory = new Category();
        sampleCategory.setId(1L);
        sampleCategory.setName("Electronics");
        sampleCategory.setDescription("Electronics category");
        sampleCategory.setStatus(true);
        sampleCategory.setStore(sampleStore);
        sampleCategory.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testRegisterCategory() {
        when(categoryRepository.save(sampleCategory)).thenReturn(sampleCategory);
        Category saved = categoryService.registerCategory(sampleCategory);
        assertNotNull(saved);
        assertEquals("Electronics", saved.getName());
    }

    @Test
    void testGetCategoryByIdFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        Optional<Category> result = categoryService.getCategoryById(1L);
        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
    }

    @Test
    void testGetCategoryByIdNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Category> result = categoryService.getCategoryById(2L);
        assertFalse(result.isPresent());
    }

    @Test
    void testListActiveCategories() {
        when(categoryRepository.findByStatusTrue()).thenReturn(List.of(sampleCategory));
        List<Category> result = categoryService.listActiveCategories();
        assertEquals(1, result.size());
    }

    @Test
    void testListCategoriesByStoreDTO() {
        when(categoryRepository.findByStore(sampleStore)).thenReturn(List.of(sampleCategory));
        List<CategoryResponseDTO> result = categoryService.listCategoriesByStoreDTO(sampleStore);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    void testListActiveCategoriesByStoreDTO() {
        when(categoryRepository.findByStoreAndStatusTrue(sampleStore)).thenReturn(List.of(sampleCategory));
        List<CategoryResponseDTO> result = categoryService.listActiveCategoriesByStoreDTO(sampleStore);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getStatus());
    }

    @Test
    void testUpdateCategoryFound() {
        Category newDetails = new Category();
        newDetails.setName("New name");
        newDetails.setDescription("New desc");
        newDetails.setStatus(false);
        newDetails.setStore(sampleStore);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category updated = categoryService.updateCategory(1L, newDetails);

        assertEquals("New name", updated.getName());
        assertEquals("New desc", updated.getDescription());
        assertFalse(updated.getStatus());
    }

    @Test
    void testUpdateCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                categoryService.updateCategory(999L, sampleCategory)
        );

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);
        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
