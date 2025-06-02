package com.tdd.billing.controllers;
import com.tdd.billing.dto.SaleRequestDTO;
import com.tdd.billing.dto.SaleResponseDto;
import com.tdd.billing.entities.Sale;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.services.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleService saleService;

    private Sale sampleSale;

    @BeforeEach
    void setUp() {
        sampleSale = new Sale();
        sampleSale.setId(1L);
    }

    @Test
    void createSale_shouldReturnCreatedSale() {
        SaleRequestDTO requestDTO = new SaleRequestDTO();
        when(saleService.createSale(requestDTO)).thenReturn(sampleSale);

        ResponseEntity<Sale> response = saleController.createSale(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleSale, response.getBody());
    }

    @Test
    void listActiveSales_shouldReturnList() {
        List<Sale> sales = List.of(sampleSale);
        when(saleService.listarVentasActivas()).thenReturn(sales);

        ResponseEntity<List<Sale>> response = saleController.listActiveSales();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sales, response.getBody());
    }

    @Test
    void getSaleById_found_shouldReturnSale() {
        when(saleService.buscarPorId(1L)).thenReturn(Optional.of(sampleSale));

        ResponseEntity<Sale> response = saleController.getSaleById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleSale, response.getBody());
    }

    @Test
    void getSaleById_notFound_shouldReturn404() {
        when(saleService.buscarPorId(1L)).thenReturn(Optional.empty());

        ResponseEntity<Sale> response = saleController.getSaleById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getSalesByUser_shouldReturnList() {
        List<Sale> sales = List.of(sampleSale);
        when(saleService.buscarPorUsuario(10L)).thenReturn(sales);

        ResponseEntity<List<Sale>> response = saleController.getSalesByUser(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sales, response.getBody());
    }

    @Test
    void getSalesByStore_shouldReturnList() {
        Store store = new Store();
        store.setId(20L);
        sampleSale.setStore(store);  // Inicializa store

        User user = new User();
        user.setId(1L);
        sampleSale.setUser(user);    // Inicializa user

        List<Sale> sales = List.of(sampleSale);
        when(saleService.findByStore(20L)).thenReturn(sales);

        ResponseEntity<List<SaleResponseDto>> response = saleController.getSalesByStore(20L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Validaciones opcionales
        assertFalse(response.getBody().isEmpty());
        assertEquals(20L, response.getBody().get(0).getStoreId());
        assertEquals(1L, response.getBody().get(0).getUserId()); // Si tienes este campo en SaleResponseDto
    }




    @Test
    void searchSalesByDateRange_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now().minusDays(5);
        LocalDateTime end = LocalDateTime.now();
        List<Sale> sales = List.of(sampleSale);

        when(saleService.buscarPorRangoFechas(start, end)).thenReturn(sales);

        ResponseEntity<List<Sale>> response = saleController.searchSalesByDateRange(start.toString(), end.toString());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sales, response.getBody());
    }

    @Test
    void updateSale_shouldReturnUpdatedSale() {
        Sale updatedSale = new Sale();
        updatedSale.setId(1L);
        updatedSale.setStatus(Sale.SaleStatus.PENDING);

        when(saleService.actualizarVenta(eq(1L), any(Sale.class))).thenReturn(updatedSale);

        ResponseEntity<Sale> response = saleController.updateSale(1L, updatedSale);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedSale, response.getBody());
    }

    @Test
    void cancelSale_shouldReturnCancelledSale() {
        Sale cancelledSale = new Sale();
        cancelledSale.setId(1L);
        cancelledSale.setStatus(Sale.SaleStatus.CANCELLED);

        when(saleService.cancelarVenta(1L)).thenReturn(cancelledSale);

        ResponseEntity<Sale> response = saleController.cancelSale(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cancelledSale, response.getBody());
    }
}
