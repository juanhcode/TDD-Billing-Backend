package com.tdd.billing.services;

import com.tdd.billing.repositories.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Captor
    private ArgumentCaptor<LocalDateTime> startCaptor;

    @Captor
    private ArgumentCaptor<LocalDateTime> endCaptor;

    @Test
    void getSalesToday_shouldCallRepositoryWithCorrectRangeAndReturnValue() {
        // Arrange
        long expectedCount = 10L;
        when(saleRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(expectedCount);

        // Act
        Long actual = statisticsService.getSalesToday();

        // Assert
        assertEquals(expectedCount, actual);

        verify(saleRepository).countByCreatedAtBetween(startCaptor.capture(), endCaptor.capture());

        LocalDateTime start = startCaptor.getValue();
        LocalDateTime end = endCaptor.getValue();

        // Asegura que las horas sean exactamente 00:00:00 y 23:59:59
        assertEquals(0, start.getHour());
        assertEquals(0, start.getMinute());
        assertEquals(0, start.getSecond());

        assertEquals(23, end.getHour());
        assertEquals(59, end.getMinute());
        assertEquals(59, end.getSecond());

        // Verifica que el rango sea del mismo día
        assertEquals(start.toLocalDate(), end.toLocalDate());
    }
}
