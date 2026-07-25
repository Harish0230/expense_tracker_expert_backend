package com.ameena.expensetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {
    @NotBlank
    private String title;

    @NotNull @Positive
    private BigDecimal amount;

    @NotBlank
    private String type; // income | expense

    @NotNull
    private Long categoryId;

    private String notes;

    @NotNull
    private LocalDate date;
}
