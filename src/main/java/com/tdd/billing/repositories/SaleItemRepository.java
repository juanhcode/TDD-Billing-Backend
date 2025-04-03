package com.tdd.billing.repositories;

import com.tdd.billing.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    // Búsqueda de items por venta
    List<SaleItem> findBySaleId(Long saleId);

    // Búsqueda de items por producto
    List<SaleItem> findByProductId(Long productId);

    // Búsqueda de items por rango de precios unitarios
    List<SaleItem> findByUnitPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Cálculo de cantidad total vendida por producto
    @Query("SELECT SUM(i.quantity) FROM SaleItem i WHERE i.product.id = :productId")
    Optional<Integer> getTotalSoldQuantityByProduct(@Param("productId") Long productId);

    // Cálculo de ingresos totales por producto
    @Query("SELECT SUM(i.subtotal) FROM SaleItem i WHERE i.product.id = :productId")
    Optional<BigDecimal> getTotalRevenueByProduct(@Param("productId") Long productId);

    // Búsqueda de items en ventas activas (no canceladas)
    @Query("SELECT i FROM SaleItem i WHERE i.sale.status <> 'CANCELLED'")
    List<SaleItem> findItemsInActiveSales();

    // Actualización masiva de precios unitarios para un producto
    @Modifying
    @Query("UPDATE SaleItem i SET i.unitPrice = :newPrice, i.subtotal = (i.quantity * :newPrice) " +
            "WHERE i.product.id = :productId")
    int updatePricesForProduct(@Param("productId") Long productId,
                               @Param("newPrice") BigDecimal newPrice);

    // Eliminación de items por venta (para transacciones de cancelación)
    @Modifying
    @Query("DELETE FROM SaleItem i WHERE i.sale.id = :saleId")
    void deleteBySaleId(@Param("saleId") Long saleId);


    // Método para verificar si un producto existe en alguna venta
    boolean existsByProductId(Long productId);

    // Top productos más vendidos
    @Query("SELECT i.product, SUM(i.quantity) as totalQuantity " +
            "FROM SaleItem i " +
            "GROUP BY i.product " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProducts();
}
