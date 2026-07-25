package com.ameena.expensetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {
    private BigDecimal incomeTarget;
    private BigDecimal expenseLimit;
    private BigDecimal savingsGoal;
    private List<AllocationItem> allocations;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllocationItem {
        private Long categoryId;
        private Double percent;
    }
}
