package com.tdd.billing.services;

import com.tdd.billing.dto.InvoiceRequestDTO;
import com.tdd.billing.dto.InvoiceResponseDTO;
import com.tdd.billing.entities.Invoice;
import com.tdd.billing.repositories.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private InvoiceRequestDTO requestDTO;
    private Invoice savedInvoice;

    @BeforeEach
    void setUp() {
        requestDTO = new InvoiceRequestDTO();
        requestDTO.setSaleId(1L);
        requestDTO.setStoreId(10L);
        requestDTO.setSubtotal(BigDecimal.valueOf(100.00));
        requestDTO.setTaxes(BigDecimal.valueOf(19.00));
        requestDTO.setTotal(BigDecimal.valueOf(119.00));
        requestDTO.setIssueDate(LocalDateTime.now());
        requestDTO.setInvoiceNumber("INV-001");
        requestDTO.setPaymentMethod("CARD");
        requestDTO.setStatus("ACTIVE");

        savedInvoice = new Invoice();
        savedInvoice.setId(123L);
        savedInvoice.setSaleId(requestDTO.getSaleId());
        savedInvoice.setStoreId(requestDTO.getStoreId());
        savedInvoice.setSubtotal(requestDTO.getSubtotal());
        savedInvoice.setTaxes(requestDTO.getTaxes());
        savedInvoice.setTotal(requestDTO.getTotal());
        savedInvoice.setIssueDate(requestDTO.getIssueDate());
        savedInvoice.setInvoiceNumber(requestDTO.getInvoiceNumber());
        savedInvoice.setPaymentMethod(requestDTO.getPaymentMethod());
        savedInvoice.setStatus(requestDTO.getStatus());
    }

    @Test
    void shouldCreateInvoiceSuccessfully() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        InvoiceResponseDTO response = invoiceService.createInvoice(requestDTO);

        assertNotNull(response);
        assertEquals(123L, response.getId());
        assertEquals("INV-001", response.getInvoiceNumber());
        assertEquals(BigDecimal.valueOf(119.00), response.getTotal());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void shouldCreateInvoiceWithDifferentPaymentMethod() {
        requestDTO.setPaymentMethod("CASH");

        savedInvoice.setPaymentMethod("CASH");
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        InvoiceResponseDTO response = invoiceService.createInvoice(requestDTO);

        assertEquals("CASH", response.getPaymentMethod());
    }

    @Test
    void shouldMapAllFieldsCorrectly() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(savedInvoice);

        InvoiceResponseDTO response = invoiceService.createInvoice(requestDTO);

        assertAll(
                () -> assertEquals(savedInvoice.getSaleId(), response.getSaleId()),
                () -> assertEquals(savedInvoice.getStoreId(), response.getStoreId()),
                () -> assertEquals(savedInvoice.getSubtotal(), response.getSubtotal()),
                () -> assertEquals(savedInvoice.getTaxes(), response.getTaxes()),
                () -> assertEquals(savedInvoice.getTotal(), response.getTotal()),
                () -> assertEquals(savedInvoice.getIssueDate(), response.getIssueDate()),
                () -> assertEquals(savedInvoice.getInvoiceNumber(), response.getInvoiceNumber()),
                () -> assertEquals(savedInvoice.getPaymentMethod(), response.getPaymentMethod()),
                () -> assertEquals(savedInvoice.getStatus(), response.getStatus())
        );
    }


}
