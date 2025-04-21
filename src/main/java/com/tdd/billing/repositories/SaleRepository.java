package com.tdd.billing.repositories;

import com.tdd.billing.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // Búsqueda por cliente
    List<Sale> findByUser(User user);

    // Búsqueda por tienda
    List<Sale> findByStore(Store store);

    // Búsqueda por estado
    List<Sale> findByStatus(Sale.SaleStatus status);

    // Búsqueda de ventas activas (no canceladas)
    @Query("SELECT s FROM Sale s WHERE s.status <> 'CANCELLED'")
    List<Sale> findActiveSales();

    // Búsqueda por rango de fechas
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Búsqueda por método de pago
    List<Sale> findByPaymentMethod(String paymentMethod);

    // Búsqueda de ventas por producto (usando items de venta)
    @Query("SELECT DISTINCT s FROM Sale s JOIN s.items i WHERE i.product.id = :productId")
    List<Sale> findByProductId(@Param("productId") Long productId);

    // Obtener el total de ventas por período
    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    Optional<BigDecimal> getTotalSalesByPeriod(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);

    // Método para cancelar venta
    @Modifying
    @Query("UPDATE Sale s SET s.status = 'CANCELLED' WHERE s.id = :id AND s.status <> 'CANCELLED'")
    int cancelSale(@Param("id") Long id);

    // Método para obtener items de una venta específica
    @Query("SELECT i FROM SaleItem i WHERE i.sale.id = :saleId")
    List<SaleItem> findItemsBySaleId(@Param("saleId") Long saleId);

    // Método para verificar si un producto está en ventas activas
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM SaleItem i WHERE i.product.id = :productId AND i.sale.status <> 'CANCELLED'")
    boolean existsByProductInActiveSales(@Param("productId") Long productId);

    // Método para obtener las ventas más recientes
    List<Sale> findByStatusNot(Sale.SaleStatus status);
}
