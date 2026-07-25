package com.expensetracker.smart_expense_tracker.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    // Static rates relative to USD (base currency). Update manually as needed.
    private static final Map<String, Double> RATES_TO_USD = Map.of(
            "USD", 1.0,
            "INR", 0.012,
            "EUR", 1.08,
            "GBP", 1.27,
            "JPY", 0.0067,
            "AUD", 0.65
    );

    public double convertToUSD(double amount, String currency) {
        Double rate = RATES_TO_USD.get(currency.toUpperCase());
        if (rate == null) {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
        return amount * rate;
    }
}