package com.tdd.billing.services;
import com.tdd.billing.dto.NotificationRequestDTO;
import com.tdd.billing.dto.SaleRequestDTO;
import com.tdd.billing.entities.Sale;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.repositories.SaleRepository;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private SaleService saleService;

    private User user;
    private Store store;
    private Sale sale;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        store = new Store();
        store.setId(1L);

        sale = new Sale();
        sale.setId(1L);
        sale.setUser(user);
        sale.setStore(store);
        sale.setStatus(Sale.SaleStatus.COMPLETED);
        sale.setTotalAmount(new BigDecimal("100.00"));
        sale.setPaymentMethod("CASH");
    }

    @Test
    void testCreateSale_success() {
        SaleRequestDTO dto = new SaleRequestDTO();
        dto.setUserId(1L);
        dto.setStoreId(1L);
        dto.setPaymentMethod("CASH");
        dto.setTotalAmount(new BigDecimal("100.00"));
        dto.setStatus(Sale.SaleStatus.COMPLETED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale result = saleService.createSale(dto);

        assertNotNull(result);
        assertEquals(sale.getId(), result.getId());

        verify(notificationService, times(1)).crearNotificacionDTO(any(NotificationRequestDTO.class));
    }

    @Test
    void testListarVentasActivas() {
        when(saleRepository.findByStatusNot(Sale.SaleStatus.CANCELLED)).thenReturn(List.of(sale));

        List<Sale> result = saleService.listarVentasActivas();

        assertEquals(1, result.size());
    }

    @Test
    void testBuscarPorId_found() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        Optional<Sale> result = saleService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(sale.getId(), result.get().getId());
    }

    @Test
    void testActualizarVenta_success() {
        Sale cambios = new Sale();
        cambios.setPaymentMethod("CARD");

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale result = saleService.actualizarVenta(1L, cambios);

        assertEquals("CARD", result.getPaymentMethod());
    }

    @Test
    void testCancelarVenta_success() {
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale result = saleService.cancelarVenta(1L);

        assertEquals(Sale.SaleStatus.CANCELLED, result.getStatus());
    }

    @Test
    void testCancelarVenta_alreadyCancelled() {
        sale.setStatus(Sale.SaleStatus.CANCELLED);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        assertThrows(IllegalStateException.class, () -> saleService.cancelarVenta(1L));
    }

    @Test
    void testBuscarPorUsuario_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(saleRepository.findByUser(user)).thenReturn(List.of(sale));

        List<Sale> result = saleService.buscarPorUsuario(1L);

        assertEquals(1, result.size());
    }

//    @Test
//    void testBuscarPorTienda_found() {
//        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
//        when(saleRepository.findByStore(store)).thenReturn(List.of(sale));
//
//        List<Sale> result = saleService.buscarPorTienda(1L);
//
//        assertEquals(1, result.size());
//    }

    @Test
    void testBuscarPorRangoFechas() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(saleRepository.findBySaleDateBetween(start, end)).thenReturn(List.of(sale));

        List<Sale> result = saleService.buscarPorRangoFechas(start, end);

        assertEquals(1, result.size());
    }
}
