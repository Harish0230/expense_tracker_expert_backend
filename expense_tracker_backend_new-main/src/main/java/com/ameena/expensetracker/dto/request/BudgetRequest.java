package com.ameena.expensetracker.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BudgetRequest {
    private BigDecimal incomeTarget;
    private BigDecimal expenseLimit;
    private BigDecimal savingsGoal;
    private List<AllocationItem> allocations;

    @Data
    public static class AllocationItem {
        private Long categoryId;
        private Double percent;
    }
}
