package com.ameena.expensetracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotBlank
    private String type; // info | warning | danger | success
}
