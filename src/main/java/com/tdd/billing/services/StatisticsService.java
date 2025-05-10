package com.tdd.billing.services;
import com.tdd.billing.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class StatisticsService {

    @Autowired
    private SaleRepository saleRepository;

    public Long getSalesToday() {
        return saleRepository.countByCreatedAtBetween(
                LocalDateTime.now().withHour(0).withMinute(0).withSecond(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59)
        );
    }


}
