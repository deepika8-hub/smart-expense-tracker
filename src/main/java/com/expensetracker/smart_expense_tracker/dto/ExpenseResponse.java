package com.expensetracker.smart_expense_tracker.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpenseResponse {
    private Long id;
    private String title;
    private Double amount;
    private String currency;
    private Double amountInUSD;
    private String category;
    private LocalDate date;
}