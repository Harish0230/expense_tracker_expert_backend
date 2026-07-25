package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.response.AnalyticsResponse;
import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.service.AnalyticsService;
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
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Spending trends and category analytics")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get analytics for the last N months (default 6)")
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getAnalytics(
            @RequestParam(defaultValue = "6") int months,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getAnalytics(user, months)));
    }
}
