package com.ophelos.incomeexpenditure.web.controllers;

/**
 * Thrown to indicate some data failed validation.
 * For example, a string containing alphabets is entered where a number is expected.
 */
public class DataValidationException extends RuntimeException {

    public DataValidationException(String message) {
        super(message);
    }

}
