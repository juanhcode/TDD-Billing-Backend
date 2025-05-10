package com.tdd.billing.controllers;
import com.tdd.billing.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/sales/today")
    public ResponseEntity<Long> getSalesToday() {
        return ResponseEntity.ok(statisticsService.getSalesToday());
    }
}
