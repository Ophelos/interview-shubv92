package com.ophelos.incomeexpenditure.api;

/**
 * Statement interface. Represents a I&E statement.
 */
public interface Statement {

    // Statement ID
    public Long getId();

    // User ID
    public Long getUserId();

    // Date of the statement
    public String getReportedDate();

}
