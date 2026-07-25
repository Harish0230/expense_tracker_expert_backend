package com.ameena.expensetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String title;
    private BigDecimal amount;
    private String type;
    private Long categoryId;
    private String categoryName;
    private String notes;
    private LocalDate date;
    private LocalDateTime createdAt;
}
