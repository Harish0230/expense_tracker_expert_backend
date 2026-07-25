package com.ameena.expensetracker.repository;

import com.ameena.expensetracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    Optional<Transaction> findByIdAndUserId(
            Long id,
            Long userId);

    List<Transaction> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId);

    void deleteByUserIdAndCategoryId(
            Long userId,
            Long categoryId);

    @Query("""
                SELECT t
                FROM Transaction t
                LEFT JOIN FETCH t.category
                WHERE t.user.id = :userId
                AND t.date BETWEEN :startDate AND :endDate
            """)
    List<Transaction> findDashboardTransactions(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Transaction> findByUserIdAndDateBetween(Long id, LocalDate startDate, LocalDate endDate);
}