package com.ophelos.incomeexpenditure.web.data;

import com.ophelos.incomeexpenditure.api.Income;

/**
 * Income implementation class for controller testing.
 */
public class IncomeImpl implements Income {

    public String category;
    public Integer amount;

    public IncomeImpl(String category, Integer amount) {
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
