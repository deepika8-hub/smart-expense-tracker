package com.expensetracker.smart_expense_tracker.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.smart_expense_tracker.dto.ExpenseRequest;
import com.expensetracker.smart_expense_tracker.dto.ExpenseResponse;
import com.expensetracker.smart_expense_tracker.dto.SummaryResponse;
import com.expensetracker.smart_expense_tracker.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(Authentication auth, @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.addExpense(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(Authentication auth) {
        return ResponseEntity.ok(expenseService.getAllExpenses(auth.getName()));
    }

    @GetMapping("/month")
    public ResponseEntity<List<ExpenseResponse>> getByMonth(
            Authentication auth,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return ResponseEntity.ok(expenseService.getExpensesByMonth(auth.getName(), year, month));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            Authentication auth, @PathVariable Long id, @Valid @RequestBody ExpenseRequest request
    ) {
        return ResponseEntity.ok(expenseService.updateExpense(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(Authentication auth, @PathVariable Long id) {
        expenseService.deleteExpense(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<SummaryResponse> getSummary(Authentication auth) {
        return ResponseEntity.ok(expenseService.getSummary(auth.getName()));
    }
}