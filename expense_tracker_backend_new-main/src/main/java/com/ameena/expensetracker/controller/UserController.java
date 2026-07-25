package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.request.ChangePasswordRequest;
import com.ameena.expensetracker.dto.request.ProfileRequest;
import com.ameena.expensetracker.dto.request.SettingsRequest;
import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.dto.response.UserResponse;
import com.ameena.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile and settings management")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(ApiResponse.success(userService.getProfile(principal.getUsername())));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update profile (name, phone, email)")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated",
                userService.updateProfile(principal.getUsername(), request)));
    }

    @PutMapping("/settings")
    @Operation(summary = "Update app settings (theme, date format, notifications)")
    public ResponseEntity<ApiResponse<UserResponse>> updateSettings(
            @AuthenticationPrincipal UserDetails principal,
            @RequestBody SettingsRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Settings updated",
                userService.updateSettings(principal.getUsername(), request)));
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed", null));
    }
}
