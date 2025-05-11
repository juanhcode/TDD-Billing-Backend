package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.*;
import com.tdd.billing.services.SaleService;
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
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleItemRepository saleItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SaleService saleService;

    private Sale sale;
    private User user;
    private Store store;
    private Product product;
    private SaleItem saleItem;

//    @BeforeEach
//    void setUp() {
//        user = new User(1L, "cliente@example.com", "Cliente Ejemplo", "password", UserRole.CUSTOMER,"", true,"12121", LocalDateTime.now());
//        store = new Store(1L,"Tienda Principal", "http://tienda.com", "Descripción", true, LocalDateTime.now(), "Dirección");
//        product = new Product(1L, store, null, "Laptop HP", "Modelo Elite", BigDecimal.valueOf(1500), 10, true, LocalDateTime.now());
//
//        sale = new Sale();
//        sale.setId(1L);
//        sale.setUser(user);
//        sale.setStore(store);
//        sale.setTotalAmount(BigDecimal.valueOf(1500));
//        sale.setStatus(Sale.SaleStatus.COMPLETED);
//        sale.setPaymentMethod("TARJETA");
//        sale.setSaleDate(LocalDateTime.now());
//
//        saleItem = new SaleItem();
//        saleItem.setId(1L);
//        saleItem.setSale(sale);
//        saleItem.setProduct(product);
//        saleItem.setQuantity(1);
//        saleItem.setUnitPrice(product.getPrice());
//        saleItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(saleItem.getQuantity())));
//    }

    @Test
    void crearVenta() {
        // Configurar mocks
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(saleItemRepository.save(any(SaleItem.class))).thenReturn(saleItem);

        Sale nuevaVenta = new Sale();
        nuevaVenta.setUser(user);
        nuevaVenta.setStore(store);
        nuevaVenta.setItems(List.of(saleItem));

        Sale resultado = saleService.createSale(nuevaVenta);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(saleRepository, times(2)).save(any(Sale.class));
        verify(saleItemRepository, times(1)).save(any(SaleItem.class));
    }

    @Test
    void buscarVentaPorId() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        Optional<Sale> resultado = saleService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("TARJETA", resultado.get().getPaymentMethod());
    }

    @Test
    void listarVentasActivas() {
        when(saleRepository.findByStatusNot(Sale.SaleStatus.CANCELLED)).thenReturn(List.of(sale));

        List<Sale> resultado = saleService.listarVentasActivas();

        assertEquals(1, resultado.size());
        assertEquals(Sale.SaleStatus.COMPLETED, resultado.get(0).getStatus());
    }

    @Test
    void cancelarVenta() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale resultado = saleService.cancelarVenta(1L);

        assertEquals(Sale.SaleStatus.CANCELLED, resultado.getStatus());
        verify(saleRepository, times(1)).save(sale);
    }

    @Test
    void cancelarVenta_yaCancelada() {
        sale.setStatus(Sale.SaleStatus.CANCELLED);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        assertThrows(IllegalStateException.class, () -> {
            saleService.cancelarVenta(1L);
        });

        verify(saleRepository, never()).save(any());
    }

    @Test
    void buscarPorUsuario() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(saleRepository.findByUser(user)).thenReturn(List.of(sale));

        List<Sale> resultado = saleService.buscarPorUsuario(1L);

        assertEquals(1, resultado.size());
        verify(saleRepository, times(1)).findByUser(user);
    }

    @Test
    void buscarPorTienda() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(saleRepository.findByStore(store)).thenReturn(List.of(sale));

        List<Sale> resultado = saleService.buscarPorTienda(1L);

        assertEquals(1, resultado.size());
        verify(saleRepository, times(1)).findByStore(store);
    }

    @Test
    void obtenerItemsDeVenta() {
        when(saleRepository.existsById(1L)).thenReturn(true);
        when(saleItemRepository.findBySaleId(1L)).thenReturn(List.of(saleItem));

        List<SaleItem> resultado = saleService.obtenerItemsDeVenta(1L);

        assertEquals(1, resultado.size());
        assertEquals("Laptop HP", resultado.get(0).getProduct().getName());
    }

    @Test
    void eliminarVenta() {
        // 1. Configurar datos de prueba
        Sale ventaCancelada = new Sale();
        ventaCancelada.setStatus(Sale.SaleStatus.CANCELLED);
        ventaCancelada.setId(1L);

        // 2. Configurar mocks
        when(saleRepository.findById(1L)).thenReturn(Optional.of(ventaCancelada));

        // Configurar comportamiento para métodos void
        doNothing().doThrow(new RuntimeException("Error en segunda llamada")).when(saleItemRepository).deleteBySaleId(1L);
        doNothing().when(saleRepository).deleteById(1L);

        // 3. Ejecutar el método a probar
        saleService.eliminarVenta(1L);

        // 4. Verificaciones
        verify(saleItemRepository, times(1)).deleteBySaleId(1L);
        verify(saleRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarVenta_noCancelada() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        assertThrows(IllegalStateException.class, () -> {
            saleService.eliminarVenta(1L);
        });

        verify(saleRepository, never()).delete(any());
    }
}
