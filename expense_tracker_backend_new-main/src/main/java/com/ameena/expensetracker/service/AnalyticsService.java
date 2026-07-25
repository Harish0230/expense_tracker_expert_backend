package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.response.AnalyticsResponse;
import com.ameena.expensetracker.entity.Transaction;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final TransactionRepository transactionRepository;

    public AnalyticsResponse getAnalytics(User user, int months) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months).withDayOfMonth(1);

        List<Transaction> transactions = transactionRepository
                .findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        // Monthly trends
        Map<String, BigDecimal> monthlyIncome = new LinkedHashMap<>();
        Map<String, BigDecimal> monthlyExpense = new LinkedHashMap<>();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        for (Transaction t : transactions) {
            String key = t.getDate().format(fmt);
            if (t.getType() == Transaction.TxnType.income) {
                monthlyIncome.merge(key, t.getAmount(), BigDecimal::add);
            } else {
                monthlyExpense.merge(key, t.getAmount(), BigDecimal::add);
            }
        }

        Set<String> allMonths = new TreeSet<>();
        allMonths.addAll(monthlyIncome.keySet());
        allMonths.addAll(monthlyExpense.keySet());

        List<AnalyticsResponse.MonthlyTrend> trends = allMonths.stream()
                .map(m -> AnalyticsResponse.MonthlyTrend.builder()
                        .month(m)
                        .income(monthlyIncome.getOrDefault(m, BigDecimal.ZERO))
                        .expense(monthlyExpense.getOrDefault(m, BigDecimal.ZERO))
                        .build())
                .collect(Collectors.toList());

        // Expense by category
        Map<String, BigDecimal> expenseByCategory = transactions.stream()
                .filter(t -> t.getType() == Transaction.TxnType.expense)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        // Income by category
        Map<String, BigDecimal> incomeByCategory = transactions.stream()
                .filter(t -> t.getType() == Transaction.TxnType.income)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        // Average daily spend
        List<Transaction> expenses = transactions.stream()
                .filter(t -> t.getType() == Transaction.TxnType.expense)
                .toList();
        BigDecimal totalExpense = expenses.stream()
                .map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        long days = Math.max(1, startDate.until(endDate).getDays());
        BigDecimal avgDailySpend = totalExpense.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);

        // Highest single expense
        BigDecimal highestExpense = expenses.stream()
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        // Top category
        String topCategory = expenseByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("N/A");

        return AnalyticsResponse.builder()
                .monthlyTrends(trends)
                .expenseByCategory(expenseByCategory)
                .incomeByCategory(incomeByCategory)
                .averageDailySpend(avgDailySpend)
                .highestExpense(highestExpense)
                .topCategory(topCategory)
                .build();
    }
}
