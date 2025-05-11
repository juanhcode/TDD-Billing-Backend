package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.ProductRepository;
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

    @InjectMocks
    private ProductService productService;

    private Store store;
    private Category category;
    private Product product;

//    @BeforeEach
//    void setUp() {
//        store = new Store(1L,"Tienda Principal", "http://tienda.com", "Descripción", true, LocalDateTime.now(), "Dirección");
//        category = new Category(1L, store, "Electrónicos", "Descripción", true, LocalDateTime.now());
//        product = new Product();
//        product.setId(1L);
//        product.setStore(store);
//        product.setCategory(category);
//        product.setName("Laptop HP");
//        product.setDescription("Modelo EliteBook");
//        product.setPrice(BigDecimal.valueOf(1500.99));
//        product.setStock(10);
//        product.setStatus(true);
//        product.setCreatedAt(LocalDateTime.now());
//    }

    @Test
    void testRegisterProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.registerProduct(product);

        assertNotNull(saved);
        assertEquals("Laptop HP", saved.getName());
        verify(productRepository).save(product);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> found = productService.getProductById(1L);

        assertTrue(found.isPresent());
        assertEquals("Laptop HP", found.get().getName());
    }

    @Test
    void testListActiveProducts() {
        when(productRepository.findByStatusTrue()).thenReturn(List.of(product));

        List<Product> products = productService.listActiveProducts();

        assertFalse(products.isEmpty());
        assertEquals(1, products.size());
    }

    @Test
    void testListProductsByStore() {
        when(productRepository.findByStore(store)).thenReturn(List.of(product));

        List<Product> products = productService.listProductsByStore(store);

        assertEquals(1, products.size());
        assertEquals(store, products.get(0).getStore());
    }

    @Test
    void testListActiveProductsByStore() {
        when(productRepository.findByStoreAndStatusTrue(store)).thenReturn(List.of(product));

        List<Product> products = productService.listActiveProductsByStore(store);

        assertEquals(1, products.size());
        assertTrue(products.get(0).getStatus());
    }

    @Test
    void testListProductsByCategory() {
        when(productRepository.findByCategory(category)).thenReturn(List.of(product));

        List<Product> products = productService.listProductsByCategory(category);

        assertEquals(1, products.size());
        assertEquals(category, products.get(0).getCategory());
    }
    @Test
    void testUpdateProduct() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Name");
        updatedDetails.setDescription("Updated Description");
        updatedDetails.setPrice(BigDecimal.valueOf(1200.00));
        updatedDetails.setStock(5);
        updatedDetails.setStatus(false);
        updatedDetails.setStore(store);
        updatedDetails.setCategory(category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedDetails);

        Product updatedProduct = productService.updateProduct(1L, updatedDetails);

        assertNotNull(updatedProduct);
        assertEquals("Updated Name", updatedProduct.getName());
        assertFalse(updatedProduct.getStatus());
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
