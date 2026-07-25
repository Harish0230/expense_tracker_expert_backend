package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.request.TransactionRequest;
import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.dto.response.TransactionResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.service.TransactionService;
import com.ameena.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "CRUD for income and expense transactions")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all transactions (optionally filter by date range)")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAll(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        User user = userService.getByEmail(principal.getUsername());
        List<TransactionResponse> result = (start != null && end != null)
                ? transactionService.getByDateRange(user, start, end)
                : transactionService.getAll(user);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a transaction by ID")
    public ResponseEntity<ApiResponse<TransactionResponse>> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(transactionService.getById(id, user)));
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> create(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transaction created", transactionService.create(request, user)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> update(
            @PathVariable Long id,
            @RequestBody TransactionRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Transaction updated", transactionService.update(id, request, user)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        transactionService.delete(id, user);
        return ResponseEntity.ok(ApiResponse.success("Transaction deleted", null));
    }
}
