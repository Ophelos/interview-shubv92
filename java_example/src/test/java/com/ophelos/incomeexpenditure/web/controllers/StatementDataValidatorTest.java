package com.ophelos.incomeexpenditure.web.controllers;

import com.ophelos.incomeexpenditure.web.models.StatementTableModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for StatementDataValidator.
 */
public class StatementDataValidatorTest {

    private StatementDataValidator validator = new StatementDataValidator();

    @Test
    public void testValidateEmptyForm() throws Exception {
        StatementTableModel form = new StatementTableModel();
        assertDoesNotThrow(() -> validator.validate(form));
    }

    @Test
    public void testFormWithValidData() throws Exception {
        StatementTableModel form = new StatementTableModel();
        form.addRow("Salary", "2000", "Food", "500");
        form.addRow("Other", "300", "", "");
        form.addRow("", "", "Mortgage", "600");
        assertDoesNotThrow(() -> validator.validate(form));
    }

    @Test
    public void testFormWithNegativeAmount() throws Exception {
        StatementTableModel form = new StatementTableModel();
        form.addRow("Salary", "-2000", "", "");
        assertTrue(assertThrows(DataValidationException.class, () -> validator.validate(form)).getMessage()
                .contains("Amount for Salary cannot be negative."));
    }

    @Test
    public void testFormWithCategoryNoAmount() throws Exception {
        StatementTableModel form = new StatementTableModel();
        form.addRow("Salary", "", "", "");
        assertTrue(assertThrows(DataValidationException.class, () -> validator.validate(form)).getMessage()
                .contains("Amount for Salary cannot be empty"));
    }

    @Test
    public void testFormWithLargeAmount() throws Exception {
        StatementTableModel form = new StatementTableModel();
        form.addRow("Salary", "2000000000000000000000000000", "", "");
        assertTrue(assertThrows(DataValidationException.class, () -> validator.validate(form)).getMessage()
                .contains("Amount for Salary is too large."));
    }

    @Test
    public void testFormWithAmountNoCategory() throws Exception {
        StatementTableModel form = new StatementTableModel();
        form.addRow("", "2000", "", "");
        assertTrue(assertThrows(DataValidationException.class, () -> validator.validate(form)).getMessage()
                .contains("Found amount 2000 without a category"));
    }

    @Test
    public void testFormWithAlphabetAmount() throws Exception {
        StatementTableModel form = new StatementTableModel();
        form.addRow("Salary", "abc0", "", "");
        assertTrue(assertThrows(DataValidationException.class, () -> validator.validate(form)).getMessage()
                .contains("Amount for Salary is invalid."));
    }

}
