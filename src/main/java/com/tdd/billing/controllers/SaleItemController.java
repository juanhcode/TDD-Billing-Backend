package com.tdd.billing.controllers;
import com.tdd.billing.dto.SaleItemRequestDTO;
import com.tdd.billing.dto.SaleItemResponseDTO;
import com.tdd.billing.services.SaleItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sale-items")
@RequiredArgsConstructor
public class SaleItemController {

    private final SaleItemService saleItemService;

    @PostMapping
    public ResponseEntity<SaleItemResponseDTO> createSaleItem(@RequestBody SaleItemRequestDTO requestDTO) {
        SaleItemResponseDTO responseDTO = saleItemService.createSaleItem(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
