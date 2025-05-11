package com.tdd.billing.services;
import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Store store = new Store();
        store.setId(1L);

        Category category = new Category();
        category.setId(1L);

        User user = new User();
        user.setId(1L);

        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Camisa");
        sampleProduct.setPrice(BigDecimal.valueOf(99.99));
        sampleProduct.setStock(10);
        sampleProduct.setStore(store);
        sampleProduct.setCategory(category);
        sampleProduct.setUser(user);
        sampleProduct.setStatus(true);
    }

    @Test
    void shouldRegisterProduct() {
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        Product result = productService.registerProduct(sampleProduct);

        assertNotNull(result);
        assertEquals("Camisa", result.getName());
        verify(productRepository, times(1)).save(sampleProduct);
    }

    @Test
    void shouldReturnProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Optional<Product> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Camisa", result.get().getName());
        verify(productRepository).findById(1L);
    }

    @Test
    void shouldReturnActiveProducts() {
        List<Product> list = List.of(sampleProduct);
        when(productRepository.findByStatusTrue()).thenReturn(list);

        List<Product> result = productService.listActiveProducts();

        assertEquals(1, result.size());
        verify(productRepository).findByStatusTrue();
    }

    @Test
    void shouldReturnProductsByStore() {
        when(productRepository.findByStore(sampleProduct.getStore())).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.listProductsByStore(sampleProduct.getStore());

        assertEquals(1, result.size());
        verify(productRepository).findByStore(sampleProduct.getStore());
    }

    @Test
    void shouldReturnActiveProductsByStore() {
        when(productRepository.findByStoreAndStatusTrue(sampleProduct.getStore())).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.listActiveProductsByStore(sampleProduct.getStore());

        assertEquals(1, result.size());
        verify(productRepository).findByStoreAndStatusTrue(sampleProduct.getStore());
    }

    @Test
    void shouldReturnProductsByCategory() {
        when(productRepository.findByCategory(sampleProduct.getCategory())).thenReturn(List.of(sampleProduct));

        List<Product> result = productService.listProductsByCategory(sampleProduct.getCategory());

        assertEquals(1, result.size());
        verify(productRepository).findByCategory(sampleProduct.getCategory());
    }

    @Test
    void shouldUpdateProduct() {
        Product updated = new Product();
        updated.setName("Camisa nueva");
        updated.setDescription("Descripción actualizada");
        updated.setPrice(BigDecimal.valueOf(120.00));
        updated.setStock(5);
        updated.setStatus(true);
        updated.setStore(sampleProduct.getStore());
        updated.setCategory(sampleProduct.getCategory());

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = productService.updateProduct(1L, updated);

        assertEquals("Camisa nueva", result.getName());
        assertEquals(BigDecimal.valueOf(120.00), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenProductNotFoundForUpdate() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> productService.updateProduct(99L, sampleProduct));

        assertEquals("Product not found", thrown.getMessage());
    }

    @Test
    void shouldSoftDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(sampleProduct)).thenReturn(sampleProduct);

        productService.deleteProduct(1L);

        assertFalse(sampleProduct.getStatus());
        verify(productRepository).save(sampleProduct);
    }

    @Test
    void shouldThrowWhenProductNotFoundForDelete() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> productService.deleteProduct(99L));

        assertEquals("Product not found", thrown.getMessage());
    }
}
