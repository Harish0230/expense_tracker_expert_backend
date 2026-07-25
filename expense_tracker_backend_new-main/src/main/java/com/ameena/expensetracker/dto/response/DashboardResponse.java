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
public class DashboardResponse {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private BigDecimal savingsRate;
    private Map<String, BigDecimal> expenseByCategory;
    private List<TransactionResponse> recentTransactions;
    private BigDecimal monthlyBudgetUsedPercent;
}
