package com.tdd.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.billing.dto.InvoiceRequestDTO;
import com.tdd.billing.dto.InvoiceResponseDTO;
import com.tdd.billing.services.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
    }

    @Test
    void testCreateInvoice_Success() throws Exception {
        // Datos de prueba
        InvoiceRequestDTO requestDTO = new InvoiceRequestDTO();
        requestDTO.setSaleId(1L);
        requestDTO.setTotal(java.math.BigDecimal.valueOf(100.0));

        InvoiceResponseDTO responseDTO = new InvoiceResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setSaleId(1L);
        responseDTO.setTotal(java.math.BigDecimal.valueOf(100.0));

        // Mock del servicio
        when(invoiceService.createInvoice(requestDTO)).thenReturn(responseDTO);

        // Ejecución del test
        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.saleId").value(1L))
                .andExpect(jsonPath("$.total").value(100.0));
    }

    @Test
    void testCreateInvoice_InvalidJson_BadRequest() throws Exception {
        String invalidJson = "{ saleId: 1, total: }"; // total no tiene valor válido

        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }




}
