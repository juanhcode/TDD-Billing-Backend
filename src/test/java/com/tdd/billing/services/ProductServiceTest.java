package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Store store;
    private Category category;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        store = new Store(1L, "Tienda Principal", "Dirección", true, LocalDateTime.now());
        category = new Category(1L, store, "Electrónicos", "Descripción", true, LocalDateTime.now());
        supplier = new Supplier(1L,"Proveedor Tech", "contacto@tech.com", "555-1234","Hoyos@Sapa","Direccion del proveedor", LocalDateTime.now());

        product = new Product();
        product.setId(1L);
        product.setStore(store);
        product.setCategory(category);
        product.setSupplier(supplier);
        product.setName("Laptop HP");
        product.setDescription("Modelo EliteBook");
        product.setPrice(BigDecimal.valueOf(1500.99));
        product.setStock(10);
        product.setStatus(true);
        product.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void crearProducto() {
        // Configurar mocks para las relaciones
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product nuevoProducto = new Product();
        nuevoProducto.setStore(store);
        nuevoProducto.setCategory(category);
        nuevoProducto.setSupplier(supplier);
        nuevoProducto.setName("Laptop HP");
        nuevoProducto.setPrice(BigDecimal.valueOf(1500.99));
        nuevoProducto.setStock(10);

        Product resultado = productService.crearProducto(nuevoProducto);

        assertNotNull(resultado);
        assertEquals("Laptop HP", resultado.getName());
        assertEquals(10, resultado.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void buscarProductoPorId() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> resultado = productService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Laptop HP", resultado.get().getName());
    }

    @Test
    void buscarPorNombre() {
        when(productRepository.findByNameContainingIgnoreCase("HP")).thenReturn(List.of(product));
        List<Product> resultado = productService.buscarPorNombre("HP");

        assertEquals(1, resultado.size());
        assertEquals("Laptop HP", resultado.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("HP");
    }

    @Test
    void buscarPorCategoria() {
        // Configura el mock para categoryRepository
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findByCategory(category)).thenReturn(List.of(product));

        // Ejecuta el método
        List<Product> resultado = productService.buscarPorCategoria(1L);

        // Verificaciones
        assertEquals(1, resultado.size());
        verify(categoryRepository, times(1)).findById(1L); // Ahora sí se llamará
        verify(productRepository, times(1)).findByCategory(category);
    }

    @Test
    void buscarPorTienda() {
        // Configura el mock para storeRepository
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.findByStore(store)).thenReturn(List.of(product));

        // Ejecuta el método
        List<Product> resultado = productService.buscarPorTienda(1L);

        // Verificaciones
        assertEquals(1, resultado.size());
        verify(storeRepository, times(1)).findById(1L); // Ahora sí se llamará
        verify(productRepository, times(1)).findByStore(store);
    }

    @Test
    void listarProductosActivos() {
        when(productRepository.findByStatusTrue()).thenReturn(List.of(product));

        List<Product> resultado = productService.listarProductosActivos();

        assertEquals(1, resultado.size());
        assertEquals("Laptop HP", resultado.get(0).getName());
    }

    @Test
    void cambiarEstado_ActivarProducto() {
        // Configurar producto INACTIVO inicialmente
        product.setStatus(false);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product resultado = productService.cambiarEstado(1L);

        // Verificaciones
        assertTrue(resultado.isStatus()); // Debe quedar ACTIVO (true)
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void cambiarEstado_DesactivarProducto() {
        // Configurar producto ACTIVO inicialmente (status = true por defecto)
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product resultado = productService.cambiarEstado(1L);

        // Verificaciones
        assertFalse(resultado.isStatus()); // Debe quedar INACTIVO (false)
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void actualizarProducto() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product actualizacion = new Product();
        actualizacion.setName("Laptop Actualizada");
        actualizacion.setPrice(BigDecimal.valueOf(1600.00));
        actualizacion.setStock(5);

        Product resultado = productService.actualizarProducto(1L, actualizacion);

        assertEquals("Laptop Actualizada", resultado.getName());
        assertEquals(1600.00, resultado.getPrice().doubleValue());
        assertEquals(5, resultado.getStock());
    }

    @Test
    void eliminarProducto() {
        doNothing().when(productRepository).deleteById(1L);

        productService.eliminarProducto(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}