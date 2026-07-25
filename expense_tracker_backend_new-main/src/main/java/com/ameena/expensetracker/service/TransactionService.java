package com.ameena.expensetracker.service;

import com.ameena.expensetracker.dto.request.TransactionRequest;
import com.ameena.expensetracker.dto.response.TransactionResponse;
import com.ameena.expensetracker.entity.Category;
import com.ameena.expensetracker.entity.Transaction;
import com.ameena.expensetracker.entity.User;
import com.ameena.expensetracker.exception.BadRequestException;
import com.ameena.expensetracker.exception.ResourceNotFoundException;
import com.ameena.expensetracker.repository.CategoryRepository;
import com.ameena.expensetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public List<TransactionResponse> getAll(User user) {
        return transactionRepository.findByUserIdOrderByDateDesc(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TransactionResponse> getByDateRange(User user, LocalDate start, LocalDate end) {
        return transactionRepository.findByUserIdAndDateBetween(user.getId(), start, end)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TransactionResponse getById(Long id, User user) {
        return toResponse(findOrThrow(id, user.getId()));
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request, User user) {
        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        validateType(request.getType());

        Transaction txn = Transaction.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .type(Transaction.TxnType.valueOf(request.getType()))
                .category(category)
                .notes(request.getNotes())
                .date(request.getDate())
                .user(user)
                .build();

        return toResponse(transactionRepository.save(txn));
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request, User user) {
        Transaction txn = findOrThrow(id, user.getId());

        if (request.getTitle() != null) txn.setTitle(request.getTitle());
        if (request.getAmount() != null) txn.setAmount(request.getAmount());
        if (request.getType() != null) {
            validateType(request.getType());
            txn.setType(Transaction.TxnType.valueOf(request.getType()));
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            txn.setCategory(category);
        }
        if (request.getNotes() != null) txn.setNotes(request.getNotes());
        if (request.getDate() != null) txn.setDate(request.getDate());

        return toResponse(transactionRepository.save(txn));
    }

    @Transactional
    public void delete(Long id, User user) {
        Transaction txn = findOrThrow(id, user.getId());
        transactionRepository.delete(txn);
    }

    private void validateType(String type) {
        if (!type.equals("income") && !type.equals("expense")) {
            throw new BadRequestException("Transaction type must be 'income' or 'expense'");
        }
    }

    private Transaction findOrThrow(Long id, Long userId) {
        return transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    public TransactionResponse toResponse(Transaction txn) {
        return TransactionResponse.builder()
                .id(txn.getId())
                .title(txn.getTitle())
                .amount(txn.getAmount())
                .type(txn.getType().name())
                .categoryId(txn.getCategory().getId())
                .categoryName(txn.getCategory().getName())
                .notes(txn.getNotes())
                .date(txn.getDate())
                .createdAt(txn.getCreatedAt())
                .build();
    }
}
