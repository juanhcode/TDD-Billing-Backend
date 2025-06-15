package com.tdd.billing.sockets;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.billing.config.StatisticsWebSocketHandler;
import com.tdd.billing.dto.StatisticsDto;
import com.tdd.billing.services.SaleService;
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
    private SaleService saleService;

    @Autowired
    private StatisticsWebSocketHandler webSocketHandler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 5000)
    public void sendSalesData() {
        try {
            StatisticsDto dto = new StatisticsDto();
            dto.setSalesToday(statisticsService.getSalesToday());
            dto.setSalesThisMonth(statisticsService.getSalesThisMonth());
            dto.setIncomeToday(saleService.getDailyIncome());
            dto.setIncomeThisMonth(saleService.getMonthlyIncome());

            String json = objectMapper.writeValueAsString(dto);
            webSocketHandler.broadcast(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
