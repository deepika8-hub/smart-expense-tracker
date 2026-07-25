package com.expensetracker.smart_expense_tracker.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryResponse {
    private Double totalUSD;
    private Map<String, Double> byCategory;
}