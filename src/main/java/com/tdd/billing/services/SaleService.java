package com.tdd.billing.services;
import com.tdd.billing.dto.NotificationRequestDTO;
import com.tdd.billing.dto.SaleRequestDTO;
import com.tdd.billing.entities.Sale;
import com.tdd.billing.entities.Store;
import com.tdd.billing.entities.User;
import com.tdd.billing.repositories.SaleRepository;
import com.tdd.billing.repositories.StoreRepository;
import com.tdd.billing.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final NotificationService notificationService;

    public SaleService(SaleRepository saleRepository,
                       UserRepository userRepository,
                       StoreRepository storeRepository,
                       NotificationService notificationService) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Sale createSale(SaleRequestDTO requestSale) {
        User user = userRepository.findById(requestSale.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Store store = storeRepository.findById(requestSale.getStoreId())
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setStore(store);
        sale.setSaleDate(LocalDateTime.now());
        sale.setStatus(requestSale.getStatus());
        sale.setPaymentMethod(requestSale.getPaymentMethod());
        sale.setTotalAmount(requestSale.getTotalAmount());

        Sale savedSale = saleRepository.save(sale);

        // Crear notificación
        NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
        notificationRequest.setUserId(user.getId());
        notificationRequest.setTitle("Nueva venta creada");
        notificationRequest.setMessage("Se ha registrado una nueva venta por $" + savedSale.getTotalAmount());
        notificationRequest.setType("SALE");
        notificationRequest.setProductId(2L);

        notificationService.createNotificationDTO(notificationRequest);

        return savedSale;
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

    public List<Sale> findByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada"));
        return saleRepository.findByStore(store);
    }

    public List<Sale> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return saleRepository.findBySaleDateBetween(inicio, fin);
    }

    public BigDecimal getDailyIncome() {
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return calcularIngresosPorRango(start, end);
    }

    public BigDecimal getMonthlyIncome() {
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusMonths(1);
        return calcularIngresosPorRango(start, end);
    }


    private BigDecimal calcularIngresosPorRango(LocalDateTime start, LocalDateTime end) {
        return saleRepository.findBySaleDateBetween(start, end).stream()
                .filter(sale -> sale.getStatus() == Sale.SaleStatus.COMPLETED)
                .map(Sale::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
