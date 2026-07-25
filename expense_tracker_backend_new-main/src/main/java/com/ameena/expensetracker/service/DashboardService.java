package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.response.DashboardResponse;
import com.ameena.expensetracker.dto.response.TransactionResponse;
import com.ameena.expensetracker.entity.Budget;
import com.ameena.expensetracker.entity.Transaction;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.repository.BudgetRepository;
import com.ameena.expensetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

        private final TransactionRepository transactionRepository;
        private final BudgetRepository budgetRepository;
        private final TransactionService transactionService;

        @Transactional(readOnly = true)
        public DashboardResponse getDashboard(User user) {

                LocalDate now = LocalDate.now();
                LocalDate startOfMonth = now.withDayOfMonth(1);

                List<Transaction> monthlyTxns = transactionRepository.findDashboardTransactions(
                                user.getId(),
                                startOfMonth,
                                now);

                BigDecimal totalIncome = monthlyTxns.stream()
                                .filter(t -> t.getType() == Transaction.TxnType.income)
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalExpense = monthlyTxns.stream()
                                .filter(t -> t.getType() == Transaction.TxnType.expense)
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal balance = totalIncome.subtract(totalExpense);

                BigDecimal savingsRate = totalIncome.compareTo(BigDecimal.ZERO) > 0
                                ? balance.divide(
                                                totalIncome,
                                                4,
                                                RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                                : BigDecimal.ZERO;

                Map<String, BigDecimal> expenseByCategory = monthlyTxns.stream()
                                .filter(t -> t.getType() == Transaction.TxnType.expense)
                                .collect(Collectors.groupingBy(
                                                t -> t.getCategory().getName(),
                                                Collectors.reducing(
                                                                BigDecimal.ZERO,
                                                                Transaction::getAmount,
                                                                BigDecimal::add)));

                List<TransactionResponse> recentTransactions = transactionRepository
                                .findByUserIdOrderByDateDesc(user.getId())
                                .stream()
                                .limit(10)
                                .map(transactionService::toResponse)
                                .collect(Collectors.toList());

                BigDecimal budgetUsedPercent = BigDecimal.ZERO;

                Budget budget = budgetRepository.findByUserId(user.getId()).orElse(null);

                if (budget != null
                                && budget.getExpenseLimit() != null
                                && budget.getExpenseLimit().compareTo(BigDecimal.ZERO) > 0) {

                        budgetUsedPercent = totalExpense.divide(
                                        budget.getExpenseLimit(),
                                        4,
                                        RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(100));
                }

                return DashboardResponse.builder()
                                .totalIncome(totalIncome)
                                .totalExpense(totalExpense)
                                .balance(balance)
                                .savingsRate(
                                                savingsRate.setScale(
                                                                2,
                                                                RoundingMode.HALF_UP))
                                .expenseByCategory(expenseByCategory)
                                .recentTransactions(recentTransactions)
                                .monthlyBudgetUsedPercent(
                                                budgetUsedPercent.setScale(
                                                                2,
                                                                RoundingMode.HALF_UP))
                                .build();
        }
}