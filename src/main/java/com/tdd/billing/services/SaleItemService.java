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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaleItemService {

    private final SaleItemRepository saleItemRepository;
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Transactional
    public SaleItemResponseDTO createSaleItem(SaleItemRequestDTO request) {
        Sale sale = saleRepository.findById(request.getSaleId())
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));


        if (product.getStock() < request.getQuantity()) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + product.getName());
        }


        BigDecimal subtotal = request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity()));


        SaleItem saleItem = new SaleItem();
        saleItem.setSale(sale);
        saleItem.setProduct(product);
        saleItem.setQuantity(request.getQuantity());
        saleItem.setUnitPrice(request.getUnitPrice());
        saleItem.setSubtotal(subtotal);

        SaleItem savedItem = saleItemRepository.save(saleItem);


        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        return toResponseDTO(savedItem);
    }

    private SaleItemResponseDTO toResponseDTO(SaleItem item) {
        SaleItemResponseDTO dto = new SaleItemResponseDTO();
        dto.setId(item.getId());
        dto.setSaleId(item.getSale().getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
