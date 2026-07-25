package com.expensetracker.smart_expense_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.smart_expense_tracker.model.Expense;
import com.expensetracker.smart_expense_tracker.model.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndCategory(User user, String category);
}