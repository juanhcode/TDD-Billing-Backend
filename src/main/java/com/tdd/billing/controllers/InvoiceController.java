package com.tdd.billing.controllers;

import com.tdd.billing.dto.InvoiceRequestDTO;
import com.tdd.billing.dto.InvoiceResponseDTO;
import com.tdd.billing.services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponseDTO> create(@RequestBody InvoiceRequestDTO request) {
        InvoiceResponseDTO response = invoiceService.createInvoice(request);
        return ResponseEntity.ok(response);
    }
}
