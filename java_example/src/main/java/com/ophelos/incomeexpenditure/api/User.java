package com.ophelos.incomeexpenditure.api;

/**
 * User interface. Represents a user login account.
 */
public interface User {

    // Get userId
    public Long getId();

    // Get username
    public String getUsername();

    // Get password
    public String getPassword();

    // Get currency
    public String getCurrency();

}
