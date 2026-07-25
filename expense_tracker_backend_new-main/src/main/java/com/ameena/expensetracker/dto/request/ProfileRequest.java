package com.ameena.expensetracker.dto.request;

import lombok.Data;

@Data
public class ProfileRequest {
    private String name;
    private String email;
    private String phone;
}
