package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.request.BudgetRequest;
import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.dto.response.BudgetResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.service.BudgetService;
import com.ameena.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@Tag(name = "Budget", description = "Get and update the monthly budget plan and category allocations")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get current budget")
    public ResponseEntity<ApiResponse<BudgetResponse>> get(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(budgetService.get(user)));
    }

    @PutMapping
    @Operation(summary = "Update budget targets and allocations")
    public ResponseEntity<ApiResponse<BudgetResponse>> update(
            @RequestBody BudgetRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Budget updated", budgetService.update(request, user)));
    }
}
