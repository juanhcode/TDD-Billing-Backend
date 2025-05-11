package com.tdd.billing.services;

import com.tdd.billing.dto.InvoiceRequestDTO;
import com.tdd.billing.dto.InvoiceResponseDTO;
import com.tdd.billing.entities.Invoice;
import com.tdd.billing.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO dto) {
        Invoice invoice = new Invoice();
        invoice.setSaleId(dto.getSaleId());
        invoice.setStoreId(dto.getStoreId());
        invoice.setSubtotal(dto.getSubtotal());
        invoice.setTaxes(dto.getTaxes());
        invoice.setTotal(dto.getTotal());
        invoice.setIssueDate(dto.getIssueDate());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setPaymentMethod(dto.getPaymentMethod());
        invoice.setStatus(dto.getStatus());

        Invoice saved = invoiceRepository.save(invoice);

        InvoiceResponseDTO response = new InvoiceResponseDTO();
        response.setId(saved.getId());
        response.setSaleId(saved.getSaleId());
        response.setStoreId(saved.getStoreId());
        response.setSubtotal(saved.getSubtotal());
        response.setTaxes(saved.getTaxes());
        response.setTotal(saved.getTotal());
        response.setIssueDate(saved.getIssueDate());
        response.setInvoiceNumber(saved.getInvoiceNumber());
        response.setPaymentMethod(saved.getPaymentMethod());
        response.setStatus(saved.getStatus());

        return response;
    }
}
