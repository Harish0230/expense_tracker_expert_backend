package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.BudgetRequest;
import com.ameena.expensetracker.dto.response.BudgetResponse;
import com.ameena.expensetracker.entity.Budget;
import com.ameena.expensetracker.entity.BudgetAllocation;
import com.ameena.expensetracker.entity.Category;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.ResourceNotFoundException;
import com.ameena.expensetracker.repository.BudgetRepository;
import com.ameena.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    public BudgetResponse get(User user) {
        Budget budget = budgetRepository.findByUserId(user.getId())
                .orElse(Budget.builder()
                        .incomeTarget(BigDecimal.ZERO)
                        .expenseLimit(BigDecimal.ZERO)
                        .savingsGoal(BigDecimal.ZERO)
                        .allocations(new ArrayList<>())
                        .build());
        return toResponse(budget);
    }

    @Transactional
    public BudgetResponse update(BudgetRequest request, User user) {
        Budget budget = budgetRepository.findByUserId(user.getId())
                .orElse(Budget.builder().user(user).build());

        if (budget.getId() == null) budget.setUser(user);
        if (request.getIncomeTarget() != null) budget.setIncomeTarget(request.getIncomeTarget());
        if (request.getExpenseLimit() != null) budget.setExpenseLimit(request.getExpenseLimit());
        if (request.getSavingsGoal() != null) budget.setSavingsGoal(request.getSavingsGoal());

        if (request.getAllocations() != null) {
            budget.getAllocations().clear();
            for (BudgetRequest.AllocationItem item : request.getAllocations()) {
                Category category = categoryRepository.findByIdAndUserId(item.getCategoryId(), user.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + item.getCategoryId()));
                BudgetAllocation allocation = BudgetAllocation.builder()
                        .category(category)
                        .percent(item.getPercent())
                        .budget(budget)
                        .build();
                budget.getAllocations().add(allocation);
            }
        }

        return toResponse(budgetRepository.save(budget));
    }

    private BudgetResponse toResponse(Budget budget) {
        List<BudgetResponse.AllocationItem> allocations = budget.getAllocations() == null
                ? new ArrayList<>()
                : budget.getAllocations().stream()
                .map(a -> BudgetResponse.AllocationItem.builder()
                        .categoryId(a.getCategory().getId())
                        .percent(a.getPercent())
                        .build())
                .collect(Collectors.toList());

        return BudgetResponse.builder()
                .incomeTarget(budget.getIncomeTarget())
                .expenseLimit(budget.getExpenseLimit())
                .savingsGoal(budget.getSavingsGoal())
                .allocations(allocations)
                .build();
    }
}
