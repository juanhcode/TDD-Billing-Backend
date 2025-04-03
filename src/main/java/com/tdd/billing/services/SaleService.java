package com.tdd.billing.services;

import com.tdd.billing.entities.*;
import com.tdd.billing.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository,
                       SaleItemRepository saleItemRepository,
                       UserRepository userRepository,
                       StoreRepository storeRepository,
                       ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Sale crearVenta(Sale venta) {
        // Validar relaciones
        User user = userRepository.findById(venta.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Store store = storeRepository.findById(venta.getStore().getId())
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));

        venta.setUser(user);
        venta.setStore(store);
        venta.setSaleDate(LocalDateTime.now());
        venta.setStatus(Sale.SaleStatus.COMPLETED);

        // Guardar la venta primero para generar ID
        Sale ventaGuardada = saleRepository.save(venta);

        // Procesar items
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItem item : venta.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + item.getProduct().getId()));

            item.setSale(ventaGuardada);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            total = total.add(item.getSubtotal());
            saleItemRepository.save(item);
        }

        ventaGuardada.setTotalAmount(total);
        return saleRepository.save(ventaGuardada);
    }

    public List<Sale> listarVentasActivas() {
        return saleRepository.findByStatusNot(Sale.SaleStatus.CANCELLED);
    }

    public Optional<Sale> buscarPorId(Long id) {
        return saleRepository.findById(id);
    }

    @Transactional
    public Sale actualizarVenta(Long id, Sale cambios) {
        Sale venta = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));

        // Solo permitir actualizar ciertos campos
        if (cambios.getPaymentMethod() != null) {
            venta.setPaymentMethod(cambios.getPaymentMethod());
        }

        return saleRepository.save(venta);
    }

    @Transactional
    public Sale cancelarVenta(Long id) {
        Sale venta = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));

        if (venta.getStatus() == Sale.SaleStatus.CANCELLED) {
            throw new IllegalStateException("La venta ya está cancelada");
        }

        venta.setStatus(Sale.SaleStatus.CANCELLED);
        return saleRepository.save(venta);
    }

    public List<Sale> buscarPorUsuario(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return saleRepository.findByUser(user);
    }

    public List<Sale> buscarPorTienda(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));
        return saleRepository.findByStore(store);
    }

    public List<Sale> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return saleRepository.findBySaleDateBetween(inicio, fin);
    }

    public List<SaleItem> obtenerItemsDeVenta(Long ventaId) {
        if (!saleRepository.existsById(ventaId)) {
            throw new EntityNotFoundException("Venta no encontrada");
        }
        return saleItemRepository.findBySaleId(ventaId);
    }

    @Transactional
    public void eliminarVenta(Long id) {
        Sale venta = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));

        if (venta.getStatus() != Sale.SaleStatus.CANCELLED) {
            throw new IllegalStateException("Solo se pueden eliminar ventas canceladas");
        }

        // Primero eliminar items
        saleItemRepository.deleteBySaleId(id);
        // Luego eliminar la venta
        saleRepository.deleteById(id);
    }
}
