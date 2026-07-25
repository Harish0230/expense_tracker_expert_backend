package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.CategoryRequest;
import com.ameena.expensetracker.dto.response.CategoryResponse;
import com.ameena.expensetracker.entity.Category;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.ResourceNotFoundException;
import com.ameena.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll(User user) {
        return categoryRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CategoryResponse getById(Long id, User user) {
        return toResponse(findOrThrow(id, user.getId()));
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request, User user) {
        Category category = Category.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .color(request.getColor())
                .type(Category.CategoryType.valueOf(request.getType()))
                .user(user)
                .build();
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request, User user) {
        Category category = findOrThrow(id, user.getId());
        if (request.getName() != null) category.setName(request.getName());
        if (request.getIcon() != null) category.setIcon(request.getIcon());
        if (request.getColor() != null) category.setColor(request.getColor());
        if (request.getType() != null) category.setType(Category.CategoryType.valueOf(request.getType()));
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id, User user) {
        Category category = findOrThrow(id, user.getId());
        categoryRepository.delete(category);
    }

    private Category findOrThrow(Long id, Long userId) {
        return categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .icon(category.getIcon())
                .color(category.getColor())
                .type(category.getType().name())
                .build();
    }
}
