package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.request.GoalRequest;
import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.dto.response.GoalResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.service.GoalService;
import com.ameena.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Goals", description = "Manage financial savings goals")
@SecurityRequirement(name = "bearerAuth")
public class GoalController {

    private final GoalService goalService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all goals")
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getAll(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(goalService.getAll(user)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get goal by ID")
    public ResponseEntity<ApiResponse<GoalResponse>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(goalService.getById(id, user)));
    }

    @PostMapping
    @Operation(summary = "Create a new goal")
    public ResponseEntity<ApiResponse<GoalResponse>> create(
            @Valid @RequestBody GoalRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Goal created", goalService.create(request, user)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a goal")
    public ResponseEntity<ApiResponse<GoalResponse>> update(
            @PathVariable Long id,
            @RequestBody GoalRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Goal updated", goalService.update(id, request, user)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a goal")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        goalService.delete(id, user);
        return ResponseEntity.ok(ApiResponse.success("Goal deleted", null));
    }
}
