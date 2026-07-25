package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.NotificationRequest;
import com.ameena.expensetracker.dto.response.NotificationResponse;
import com.ameena.expensetracker.entity.Notification;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.ResourceNotFoundException;
import com.ameena.expensetracker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getAll(User user) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse create(NotificationRequest request, User user) {
        Notification notification = Notification.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .type(Notification.NotificationType.valueOf(request.getType()))
                .read(false)
                .user(user)
                .build();
        return toResponse(notificationRepository.save(notification));
    }

    @Transactional
    public NotificationResponse markRead(Long id, User user) {
        Notification notification = notificationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Transactional
    public void markAllRead(User user) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void clearAll(User user) {
        notificationRepository.deleteByUserId(user.getId());
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .type(notification.getType().name())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
