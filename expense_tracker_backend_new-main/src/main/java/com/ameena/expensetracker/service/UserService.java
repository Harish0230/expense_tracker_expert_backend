package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.ChangePasswordRequest;
import com.ameena.expensetracker.dto.request.ProfileRequest;
import com.ameena.expensetracker.dto.request.SettingsRequest;
import com.ameena.expensetracker.dto.response.UserResponse;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.BadRequestException;
import com.ameena.expensetracker.exception.ResourceNotFoundException;
import com.ameena.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse getProfile(String email) {
        return toResponse(getByEmail(email));
    }

    @Transactional
    public UserResponse updateProfile(String email, ProfileRequest request) {
        User user = getByEmail(email);
        if (request.getName() != null) user.setName(request.getName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateSettings(String email, SettingsRequest request) {
        User user = getByEmail(email);
        if (request.getTheme() != null) user.setTheme(request.getTheme());
        if (request.getDateFormat() != null) user.setDateFormat(request.getDateFormat());
        if (request.getNotifyBudget() != null) user.setNotifyBudget(request.getNotifyBudget());
        if (request.getNotifyGoals() != null) user.setNotifyGoals(request.getNotifyGoals());
        if (request.getNotifyMonthly() != null) user.setNotifyMonthly(request.getNotifyMonthly());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .theme(user.getTheme())
                .dateFormat(user.getDateFormat())
                .notifyBudget(user.getNotifyBudget())
                .notifyGoals(user.getNotifyGoals())
                .notifyMonthly(user.getNotifyMonthly())
                .build();
    }
}
