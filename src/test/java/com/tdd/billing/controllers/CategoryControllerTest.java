package com.tdd.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tdd.billing.dto.CategoryResponseDTO;
import com.tdd.billing.entities.Category;
import com.tdd.billing.entities.Store;
import com.tdd.billing.services.CategoryService;
import com.tdd.billing.repositories.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper;
    private Category category;
    private Store store;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Agregar esta línea

        store = new Store();
        store.setId(1L);

        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setDescription("Devices and gadgets");
        category.setStatus(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setStore(store);
    }

    @Test
    void testRegisterCategorySuccess() throws Exception {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(categoryService.registerCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));

        verify(storeRepository).findById(1L);
        verify(categoryService).registerCategory(any(Category.class));
    }

    @Test
    void testListActiveCategories() throws Exception {
        List<Category> categories = List.of(category);
        when(categoryService.listActiveCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void testGetCategoryByIdFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void testGetCategoryByIdNotFound() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetByStore() throws Exception {
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Electronics", "Devices", true, LocalDateTime.now());
        when(categoryService.listCategoriesByStoreDTO(any(Store.class))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/categories/store/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void testGetActiveByStore() throws Exception {
        CategoryResponseDTO dto = new CategoryResponseDTO(1L, "Electronics", "Devices", true, LocalDateTime.now());
        when(categoryService.listActiveCategoriesByStoreDTO(any(Store.class))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/categories/store/1/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category updated = new Category();
        updated.setId(1L);
        updated.setName("Updated");
        updated.setDescription("Updated Desc");
        updated.setStore(store);

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updated);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(1L);
    }
}
