package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.LoginRequest;
import com.ameena.expensetracker.dto.request.RefreshTokenRequest;
import com.ameena.expensetracker.dto.request.RegisterRequest;
import com.ameena.expensetracker.dto.response.AuthResponse;
import com.ameena.expensetracker.dto.response.UserResponse;
import com.ameena.expensetracker.entity.Budget;
import com.ameena.expensetracker.entity.Category;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.DuplicateResourceException;
import com.ameena.expensetracker.repository.BudgetRepository;
import com.ameena.expensetracker.repository.CategoryRepository;
import com.ameena.expensetracker.repository.UserRepository;
import com.ameena.expensetracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final List<String[]> DEFAULT_CATEGORIES = List.of(
            new String[]{"Food", "Utensils", "#f59e0b", "expense"},
            new String[]{"Travel", "Plane", "#3b82f6", "expense"},
            new String[]{"Shopping", "ShoppingBag", "#ec4899", "expense"},
            new String[]{"Bills", "Receipt", "#ef4444", "expense"},
            new String[]{"Entertainment", "Film", "#8b5cf6", "expense"},
            new String[]{"Health", "HeartPulse", "#10b981", "expense"},
            new String[]{"Education", "GraduationCap", "#06b6d4", "expense"},
            new String[]{"Investment", "TrendingUp", "#22c55e", "both"},
            new String[]{"Salary", "Wallet", "#14b8a6", "income"},
            new String[]{"Other", "MoreHorizontal", "#64748b", "both"}
    );

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user = userRepository.save(user);

        // seed default categories
        for (String[] c : DEFAULT_CATEGORIES) {
            Category category = Category.builder()
                    .name(c[0]).icon(c[1]).color(c[2])
                    .type(Category.CategoryType.valueOf(c[3]))
                    .user(user)
                    .build();
            categoryRepository.save(category);
        }

        // seed default budget
        Budget budget = Budget.builder()
                .incomeTarget(BigDecimal.ZERO)
                .expenseLimit(BigDecimal.ZERO)
                .savingsGoal(BigDecimal.ZERO)
                .user(user)
                .build();
        budgetRepository.save(budget);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DuplicateResourceException("User not found"));

        return buildAuthResponse(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String email = jwtService.extractUsername(request.getRefreshToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DuplicateResourceException("User not found"));

        if (!jwtService.isTokenValid(request.getRefreshToken(), user)) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid refresh token");
        }

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(toUserResponse(user))
                .build();
    }

    private UserResponse toUserResponse(User user) {
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
