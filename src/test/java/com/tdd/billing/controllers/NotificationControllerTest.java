package com.tdd.billing.controllers;

import com.tdd.billing.dto.NotificationRequestDTO;
import com.tdd.billing.dto.NotificationResponseDTO;
import com.tdd.billing.services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    private NotificationRequestDTO requestDTO;
    private NotificationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new NotificationRequestDTO();
        responseDTO = new NotificationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUserId(123L);
        responseDTO.setMessage("Test notification");
    }

    @Test
    void createNotification_shouldReturnCreatedNotification() {
        when(notificationService.createNotificationDTO(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<NotificationResponseDTO> response = notificationController.createNotification(requestDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
        verify(notificationService, times(1)).createNotificationDTO(requestDTO);
    }

    @Test
    void getNotificationsByUser_shouldReturnList() {
        List<NotificationResponseDTO> list = List.of(responseDTO);
        Long userId = 123L;

        when(notificationService.listByUser(userId)).thenReturn(list);

        ResponseEntity<List<NotificationResponseDTO>> response = notificationController.getNotificationsByUser(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody());
        verify(notificationService, times(1)).listByUser(userId);
    }
}
