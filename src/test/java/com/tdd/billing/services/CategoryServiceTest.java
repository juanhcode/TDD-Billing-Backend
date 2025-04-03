package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.*;
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

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private Store store;

    @BeforeEach
    void setUp() {
        store = new Store(1L, "Tienda Principal", "Dirección", true, LocalDateTime.now());
        category = new Category(1L, store, "Electrónicos", "Descripción", true, LocalDateTime.now());
    }

    @Test
    void crearCategoria() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category nuevaCategoria = new Category();
        nuevaCategoria.setStore(store);
        nuevaCategoria.setName("Electrónicos");
        nuevaCategoria.setDescription("Productos electrónicos");

        Category resultado = categoryService.crearCategoria(nuevaCategoria);

        assertNotNull(resultado);
        assertEquals("Electrónicos", resultado.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void buscarCategoriaPorId() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> resultado = categoryService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Electrónicos", resultado.get().getName());
    }

    @Test
    void buscarPorNombre() {
        when(categoryRepository.findByNameContainingIgnoreCase("Electr")).thenReturn(List.of(category));

        List<Category> resultado = categoryService.buscarPorNombre("Electr");

        assertEquals(1, resultado.size());
        assertEquals("Electrónicos", resultado.get(0).getName());
        verify(categoryRepository, times(1)).findByNameContainingIgnoreCase("Electr");
    }

    @Test
    void buscarPorTienda() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(categoryRepository.findByStore(store)).thenReturn(List.of(category));

        List<Category> resultado = categoryService.buscarPorTienda(1L);

        assertEquals(1, resultado.size());
        assertEquals("Electrónicos", resultado.get(0).getName());
        verify(storeRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findByStore(store);
    }

    @Test
    void listarCategoriasActivas() {
        when(categoryRepository.findByStatusTrue()).thenReturn(List.of(category));

        List<Category> resultado = categoryService.listarCategoriasActivas();

        assertEquals(1, resultado.size());
        assertEquals("Electrónicos", resultado.get(0).getName());
    }

    @Test
    void cambiarEstado_ActivarCategoria() {
        category.setStatus(false);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category resultado = categoryService.cambiarEstado(1L);

        assertTrue(resultado.isStatus());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void cambiarEstado_DesactivarCategoria() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category resultado = categoryService.cambiarEstado(1L);

        assertFalse(resultado.isStatus());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void actualizarCategoria() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category actualizacion = new Category();
        actualizacion.setName("Electrodomésticos");
        actualizacion.setDescription("Nueva descripción");

        Category resultado = categoryService.actualizarCategoria(1L, actualizacion);

        assertEquals("Electrodomésticos", resultado.getName());
        assertEquals("Nueva descripción", resultado.getDescription());
    }

    @Test
    void eliminarCategoria_Exitoso() {
        // Configura el mock para el método custom
        when(categoryRepository.deleteByIdIfNoProducts(1L)).thenReturn(1); // 1 fila afectada = éxito

        // Ejecuta el método
        categoryService.eliminarCategoria(1L);

        // Verifica que se llamó al método correcto
        verify(categoryRepository, times(1)).deleteByIdIfNoProducts(1L);
    }

}
