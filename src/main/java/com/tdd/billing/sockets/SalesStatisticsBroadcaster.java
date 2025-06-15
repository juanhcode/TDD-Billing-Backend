package com.tdd.billing.sockets;

import com.tdd.billing.config.StatisticsWebSocketHandler;
import com.tdd.billing.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SalesStatisticsBroadcaster {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private StatisticsWebSocketHandler webSocketHandler;

    @Scheduled(fixedRate = 5000) // Cada 5 segundos
    public void sendSalesData() {
        try {
            Long salesToday = statisticsService.getSalesToday();
            webSocketHandler.broadcast("Ventas de hoy: " + salesToday);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
