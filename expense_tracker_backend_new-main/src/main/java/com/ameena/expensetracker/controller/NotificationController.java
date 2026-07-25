package com.ameena.expensetracker.controller;

import com.ameena.expensetracker.dto.request.NotificationRequest;
import com.ameena.expensetracker.dto.response.ApiResponse;
import com.ameena.expensetracker.dto.response.NotificationResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.service.NotificationService;
import com.ameena.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "In-app notification management")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAll(
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(notificationService.getAll(user)));
    }

    @PostMapping
    @Operation(summary = "Create a notification (for internal/admin use)")
    public ResponseEntity<ApiResponse<NotificationResponse>> create(
            @RequestBody NotificationRequest request,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Notification created",
                notificationService.create(request, user)));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(ApiResponse.success(notificationService.markRead(id, user)));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all notifications as read")
    public ResponseEntity<ApiResponse<Void>> markAllRead(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        notificationService.markAllRead(user);
        return ResponseEntity.ok(ApiResponse.success("All marked as read", null));
    }

    @DeleteMapping
    @Operation(summary = "Clear all notifications")
    public ResponseEntity<ApiResponse<Void>> clearAll(@AuthenticationPrincipal UserDetails principal) {
        User user = userService.getByEmail(principal.getUsername());
        notificationService.clearAll(user);
        return ResponseEntity.ok(ApiResponse.success("Notifications cleared", null));
    }
}
