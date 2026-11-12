package com.ophelos.incomeexpenditure.web.controllers;

import com.ophelos.incomeexpenditure.web.models.StatementTableModel;
import com.ophelos.incomeexpenditure.web.models.StatementTableModel.StatementTableRowModel;

import java.util.List;

/**
 * Helper class for validating user-entered data.
 */
public class StatementDataValidator {

    public void validate(StatementTableModel form) throws DataValidationException {
        if (form.getRows() == null || form.getRows().isEmpty()) {
            // Nothing to validate
            return;
        }

        List<StatementTableRowModel> rows = form.getRows();
        for (StatementTableRowModel row : rows) {
            String incomeCategory = row.getIncomeCategory();
            String incomeAmount = row.getIncomeAmount();
            String expendtureCategory = row.getExpenditureCategory();
            String expendtureAmount = row.getExpenditureAmount();

            validateItem(incomeCategory, incomeAmount);
            validateItem(expendtureCategory, expendtureAmount);
        }
    }

    /**
     * Validate an individual entry item.
     *
     * @param category Category of the entry.
     * @param amount Amount of the entry.
     * @throws DataValidationException
     */
    private void validateItem(String category, String amount) throws DataValidationException {

        if (category != null && !category.trim().isEmpty()) {
            // Category is entered, now check income amount
            if (amount == null || amount.trim().isEmpty()) {
                throw new DataValidationException("Amount for " + category + " cannot be empty");
            }

            // Trim any leading or trailing blank spaces
            amount = amount.trim();
            if (amount.length() > 20) {
                throw new DataValidationException("Amount for " + category + " is too large.");
            }

            try {
                // Amount has to be integer
                Integer amountInt = Integer.valueOf(amount);

                // Amount has to be positive
                if (amountInt <= 0) {
                    throw new DataValidationException("Amount for " + category + " cannot be negative.");
                }

            } catch (NumberFormatException e) {
                throw new DataValidationException("Amount for " + category + " is invalid.");
            }

        } else {
            // No category is entered
            if (amount != null && !amount.trim().isEmpty()) {
                throw new DataValidationException("Found amount " + amount + " without a category");
            }
        }
    }

}
