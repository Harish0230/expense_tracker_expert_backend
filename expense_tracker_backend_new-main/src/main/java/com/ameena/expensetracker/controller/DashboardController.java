package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.dto.response.DashboardResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.service.DashboardService;
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
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Overview summary for the home dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get dashboard summary (current month income, expense, balance, recent txns)")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getDashboard(user)));
    }
}
