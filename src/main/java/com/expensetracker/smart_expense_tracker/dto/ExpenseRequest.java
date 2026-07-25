package com.expensetracker.smart_expense_tracker.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ExpenseRequest {
    @NotBlank
    private String title;

    @NotNull
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String category;

    @NotNull
    private LocalDate date;
}