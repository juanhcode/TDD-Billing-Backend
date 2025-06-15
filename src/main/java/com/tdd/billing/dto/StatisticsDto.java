package com.tdd.billing.dto;

import java.math.BigDecimal;

public class StatisticsDto {
    private Long salesToday;
    private BigDecimal incomeToday;
    private Long salesThisMonth;
    private BigDecimal incomeThisMonth;

    // Constructor vacío
    public StatisticsDto() {}

    // Getters y setters
    public Long getSalesToday() {
        return salesToday;
    }

    public void setSalesToday(Long salesToday) {
        this.salesToday = salesToday;
    }

    public BigDecimal getIncomeToday() {
        return incomeToday;
    }

    public void setIncomeToday(BigDecimal incomeToday) {
        this.incomeToday = incomeToday;
    }

    public Long getSalesThisMonth() {
        return salesThisMonth;
    }

    public void setSalesThisMonth(Long salesThisMonth) {
        this.salesThisMonth = salesThisMonth;
    }

    public BigDecimal getIncomeThisMonth() {
        return incomeThisMonth;
    }

    public void setIncomeThisMonth(BigDecimal incomeThisMonth) {
        this.incomeThisMonth = incomeThisMonth;
    }
}
