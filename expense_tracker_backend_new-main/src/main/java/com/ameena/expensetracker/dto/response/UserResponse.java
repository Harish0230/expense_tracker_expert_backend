package com.ameena.expensetracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String theme;
    private String dateFormat;
    private Boolean notifyBudget;
    private Boolean notifyGoals;
    private Boolean notifyMonthly;
}
