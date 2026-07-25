package com.ameena.expensetracker.dto.request;

import lombok.Data;

@Data
public class SettingsRequest {
    private String theme;        // light | dark
    private String dateFormat;   // dd/MM/yyyy | MM/dd/yyyy | yyyy-MM-dd
    private Boolean notifyBudget;
    private Boolean notifyGoals;
    private Boolean notifyMonthly;
}
