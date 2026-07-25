package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.GoalRequest;
import com.ameena.expensetracker.dto.response.GoalResponse;
import com.ameena.expensetracker.entity.Goal;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.ResourceNotFoundException;
import com.ameena.expensetracker.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    public List<GoalResponse> getAll(User user) {
        return goalRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public GoalResponse getById(Long id, User user) {
        return toResponse(findOrThrow(id, user.getId()));
    }

    @Transactional
    public GoalResponse create(GoalRequest request, User user) {
        Goal goal = Goal.builder()
                .name(request.getName())
                .target(request.getTarget())
                .saved(request.getSaved() != null ? request.getSaved() : BigDecimal.ZERO)
                .deadline(request.getDeadline())
                .user(user)
                .build();
        return toResponse(goalRepository.save(goal));
    }

    @Transactional
    public GoalResponse update(Long id, GoalRequest request, User user) {
        Goal goal = findOrThrow(id, user.getId());
        if (request.getName() != null) goal.setName(request.getName());
        if (request.getTarget() != null) goal.setTarget(request.getTarget());
        if (request.getSaved() != null) goal.setSaved(request.getSaved());
        if (request.getDeadline() != null) goal.setDeadline(request.getDeadline());
        return toResponse(goalRepository.save(goal));
    }

    @Transactional
    public void delete(Long id, User user) {
        Goal goal = findOrThrow(id, user.getId());
        goalRepository.delete(goal);
    }

    private Goal findOrThrow(Long id, Long userId) {
        return goalRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Goal not found with id: " + id));
    }

    private GoalResponse toResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .name(goal.getName())
                .target(goal.getTarget())
                .saved(goal.getSaved())
                .deadline(goal.getDeadline())
                .createdAt(goal.getCreatedAt())
                .build();
    }
}
