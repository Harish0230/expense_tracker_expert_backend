package com.ameena.expensetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String icon;

    @NotBlank
    private String color;

    @NotBlank
    private String type; // income | expense | both
}
