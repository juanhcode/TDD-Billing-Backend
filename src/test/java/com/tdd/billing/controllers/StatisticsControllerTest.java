package com.tdd.billing.controllers;

import com.tdd.billing.services.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsControllerTest {

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        // No se requiere configuración adicional por ahora
    }

    @Test
    void getSalesToday_shouldReturnCorrectValue() {
        // Arrange
        Long expectedSales = 42L;
        when(statisticsService.getSalesToday()).thenReturn(expectedSales);

        // Act
        ResponseEntity<Long> response = statisticsController.getSalesToday();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedSales, response.getBody());
        verify(statisticsService, times(1)).getSalesToday();
    }

    @Test
    void getSalesToday_whenServiceReturnsZero_shouldReturnZero() {
        when(statisticsService.getSalesToday()).thenReturn(0L);

        ResponseEntity<Long> response = statisticsController.getSalesToday();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0L, response.getBody());
        verify(statisticsService).getSalesToday();
    }

    @Test
    void getSalesToday_whenServiceReturnsNull_shouldReturnNullBody() {
        when(statisticsService.getSalesToday()).thenReturn(null);

        ResponseEntity<Long> response = statisticsController.getSalesToday();

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(statisticsService).getSalesToday();
    }
}
