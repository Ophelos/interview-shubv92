package com.ophelos.incomeexpenditure.web.data;

import com.ophelos.incomeexpenditure.api.Expenditure;

/**
 * Expenditure implementation class for controller testing.
 */
public class ExpenditureImpl implements Expenditure {

    public String category;
    public Integer amount;

    public ExpenditureImpl(String category, Integer amount) {
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String getCategory() {
        return this.category;
    }

    @Override
    public Integer getAmount() {
        return this.amount;
    }

}
