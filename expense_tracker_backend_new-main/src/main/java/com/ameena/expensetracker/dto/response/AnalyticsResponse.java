package com.ameena.expensetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private List<MonthlyTrend> monthlyTrends;
    private Map<String, BigDecimal> expenseByCategory;
    private Map<String, BigDecimal> incomeByCategory;
    private BigDecimal averageDailySpend;
    private BigDecimal highestExpense;
    private String topCategory;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyTrend {
        private String month; // yyyy-MM
        private BigDecimal income;
        private BigDecimal expense;
    }
}
