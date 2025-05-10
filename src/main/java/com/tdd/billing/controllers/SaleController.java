package com.tdd.billing.controllers;

import com.tdd.billing.entities.*;
import com.tdd.billing.services.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        return ResponseEntity.ok(saleService.crearVenta(sale));
    }

    @GetMapping
    public ResponseEntity<List<Sale>> listActiveSales() {
        return ResponseEntity.ok(saleService.listarVentasActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Optional<Sale> sale = saleService.buscarPorId(id);
        return sale.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Sale>> getSalesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(saleService.buscarPorUsuario(userId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Sale>> getSalesByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(saleService.buscarPorTienda(storeId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Sale>> searchSalesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(saleService.buscarPorRangoFechas(
                LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate))
        );
    }

    @GetMapping("/{saleId}/items")
    public ResponseEntity<List<SaleItem>> getSaleItems(@PathVariable Long saleId) {
        return ResponseEntity.ok(saleService.obtenerItemsDeVenta(saleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        return ResponseEntity.ok(saleService.actualizarVenta(id, sale));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Sale> cancelSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.cancelarVenta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }

}
