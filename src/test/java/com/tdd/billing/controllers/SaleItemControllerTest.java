package com.tdd.billing.controllers;

import com.tdd.billing.dto.SaleItemRequestDTO;
import com.tdd.billing.dto.SaleItemResponseDTO;
import com.tdd.billing.services.SaleItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleItemControllerTest {

    @InjectMocks
    private SaleItemController saleItemController;

    @Mock
    private SaleItemService saleItemService;

    private SaleItemRequestDTO requestDTO;
    private SaleItemResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new SaleItemRequestDTO();
        // puedes configurar campos si deseas
        responseDTO = new SaleItemResponseDTO();
        responseDTO.setId(1L);
        // puedes configurar más campos si existen
    }

    @Test
    void createSaleItem_shouldReturnResponseDTO() {
        when(saleItemService.createSaleItem(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<SaleItemResponseDTO> response = saleItemController.createSaleItem(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(saleItemService, times(1)).createSaleItem(requestDTO);
    }
}
