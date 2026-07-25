package com.expensetracker.smart_expense_tracker.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.expensetracker.smart_expense_tracker.dto.ExpenseRequest;
import com.expensetracker.smart_expense_tracker.dto.ExpenseResponse;
import com.expensetracker.smart_expense_tracker.dto.SummaryResponse;
import com.expensetracker.smart_expense_tracker.model.Expense;
import com.expensetracker.smart_expense_tracker.model.User;
import com.expensetracker.smart_expense_tracker.repository.ExpenseRepository;
import com.expensetracker.smart_expense_tracker.repository.UserRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CurrencyService currencyService;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository,
                           CurrencyService currencyService) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.currencyService = currencyService;
    }

    private User getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public ExpenseResponse addExpense(String username, ExpenseRequest request) {
        User user = getUserOrThrow(username);

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCurrency(request.getCurrency().toUpperCase());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());

        expenseRepository.save(expense);
        return toResponse(expense);
    }

    public List<ExpenseResponse> getAllExpenses(String username) {
        User user = getUserOrThrow(username);
        return expenseRepository.findByUser(user).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ExpenseResponse> getExpensesByMonth(String username, int year, int month) {
        User user = getUserOrThrow(username);
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return expenseRepository.findByUserAndDateBetween(user, start, end).stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteExpense(String username, Long expenseId) {
        User user = getUserOrThrow(username);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have permission to delete this expense");
        }

        expenseRepository.delete(expense);
    }

    public ExpenseResponse updateExpense(String username, Long expenseId, ExpenseRequest request) {
        User user = getUserOrThrow(username);
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new SecurityException("You do not have permission to edit this expense");
        }

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCurrency(request.getCurrency().toUpperCase());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate());

        expenseRepository.save(expense);
        return toResponse(expense);
    }

    public SummaryResponse getSummary(String username) {
        User user = getUserOrThrow(username);
        List<Expense> expenses = expenseRepository.findByUser(user);

        double total = 0;
        Map<String, Double> byCategory = new HashMap<>();

        for (Expense e : expenses) {
            double usdAmount = currencyService.convertToUSD(e.getAmount(), e.getCurrency());
            total += usdAmount;
            byCategory.merge(e.getCategory(), usdAmount, Double::sum);
        }

        return new SummaryResponse(total, byCategory);
    }

    private ExpenseResponse toResponse(Expense e) {
        double usdAmount = currencyService.convertToUSD(e.getAmount(), e.getCurrency());
        return new ExpenseResponse(e.getId(), e.getTitle(), e.getAmount(), e.getCurrency(),
                usdAmount, e.getCategory(), e.getDate());
    }
}