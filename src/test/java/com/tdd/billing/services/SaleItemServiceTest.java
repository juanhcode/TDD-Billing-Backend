package com.tdd.billing.services;

import com.tdd.billing.dto.SaleItemRequestDTO;
import com.tdd.billing.dto.SaleItemResponseDTO;
import com.tdd.billing.entities.Product;
import com.tdd.billing.entities.Sale;
import com.tdd.billing.entities.SaleItem;
import com.tdd.billing.repositories.ProductRepository;
import com.tdd.billing.repositories.SaleItemRepository;
import com.tdd.billing.repositories.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleItemServiceTest {

    @Mock
    private SaleItemRepository saleItemRepository;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SaleItemService saleItemService;

    private SaleItemRequestDTO requestDTO;
    private Sale sale;
    private Product product;
    private SaleItem savedSaleItem;

    @BeforeEach
    void setUp() {
        requestDTO = new SaleItemRequestDTO();
        requestDTO.setSaleId(1L);
        requestDTO.setProductId(100L);
        requestDTO.setQuantity(2);
        requestDTO.setUnitPrice(BigDecimal.valueOf(50.00));

        sale = new Sale();
        sale.setId(1L);

        product = new Product();
        product.setId(100L);
        product.setName("T-shirt");
        product.setStock(10);

        savedSaleItem = new SaleItem();
        savedSaleItem.setId(123L);
        savedSaleItem.setSale(sale);
        savedSaleItem.setProduct(product);
        savedSaleItem.setQuantity(2);
        savedSaleItem.setUnitPrice(BigDecimal.valueOf(50.00));
        savedSaleItem.setSubtotal(BigDecimal.valueOf(100.00));
    }

    @Test
    void shouldCreateSaleItemSuccessfully() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(saleItemRepository.save(any(SaleItem.class))).thenReturn(savedSaleItem);

        SaleItemResponseDTO response = saleItemService.createSaleItem(requestDTO);

        assertNotNull(response);
        assertEquals(123L, response.getId());
        assertEquals(1L, response.getSaleId());
        assertEquals(100L, response.getProductId());
        assertEquals(2, response.getQuantity());
        assertEquals(BigDecimal.valueOf(100.00), response.getSubtotal());

        verify(saleItemRepository, times(1)).save(any(SaleItem.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenSaleNotFound() {
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            saleItemService.createSaleItem(requestDTO);
        });

        assertEquals("Venta no encontrada", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productRepository.findById(100L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            saleItemService.createSaleItem(requestDTO);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {
        product.setStock(1); // menor que la cantidad requerida

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            saleItemService.createSaleItem(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Stock insuficiente para el producto"));
    }

    @Test
    void shouldReduceProductStockAfterCreatingSaleItem() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(saleItemRepository.save(any(SaleItem.class))).thenReturn(savedSaleItem);

        saleItemService.createSaleItem(requestDTO);

        verify(productRepository).save(argThat(prod -> prod.getStock() == 8)); // 10 - 2 = 8
    }
}
