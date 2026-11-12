package com.ophelos.incomeexpenditure.web.models;

import com.ophelos.incomeexpenditure.api.Expenditure;
import com.ophelos.incomeexpenditure.api.Income;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The model that contains all the necessary data to display the income-expenditure statement in a table format.
 */
public class StatementTableModel implements Serializable {

    // Statement ID number
    private String id;

    // The date the statement was reported on
    private String reportDate;

    // All the rows in the statement table
    private List<StatementTableRowModel> rows = new ArrayList<>();

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public List<StatementTableRowModel> getRows() {
        return this.rows;
    }

    public void setRows(ArrayList<StatementTableRowModel> rows) {
        this.rows = rows;
    }

    /**
     * Add the given row to table.
     * @param row
     */
    public void addRow(StatementTableRowModel row) {
        this.rows.add(row);
    }

    /**
     * Add an empty row.
     */
    public void addEmptyRow() {
        this.rows.add(new StatementTableRowModel());
    }

    /**
     * Add a row with the given data.
     *
     * @param incomeCategory
     * @param incomeAmount
     * @param expenditureCategory
     * @param expenditureAmount
     */
    public void addRow(String incomeCategory, String incomeAmount, String expenditureCategory, String expenditureAmount) {
        StatementTableRowModel row = new StatementTableRowModel();
        row.setIncomeCategory(incomeCategory);
        row.setIncomeAmount(incomeAmount);
        row.setExpenditureCategory(expenditureCategory);
        row.setExpenditureAmount(expenditureAmount);
        this.rows.add(row);
    }

    /**
     * Check if the statement table is empty.
     * @return true if table is empty; false otherwise
     */
    public boolean isEmpty() {
        return this.rows == null || this.rows.isEmpty();
    }

    public static StatementTableModelBuilder withId(String id) {
        StatementTableModelBuilder builder = new StatementTableModelBuilder();
        builder.withId(id);
        return builder;
    }

    @Override
    public String toString() {
          return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Represents a row in the statement table.
     * Each row consists of 4 columns: income category, income amount, expenditure category and expenditure amount.
     */
    public static class StatementTableRowModel implements Serializable {
        private String incomeCategory;
        private String incomeAmount;
        private String expenditureCategory;
        private String expenditureAmount;

        public StatementTableRowModel() {
        }

        public String getIncomeCategory() {
            return incomeCategory;
        }

        public void setIncomeCategory(String incomeCategory) {
            this.incomeCategory = incomeCategory;
        }

        public String getIncomeAmount() {
            return incomeAmount;
        }

        public void setIncomeAmount(String incomeAmount) {
            this.incomeAmount = incomeAmount;
        }

        public String getExpenditureCategory() {
            return expenditureCategory;
        }

        public void setExpenditureCategory(String expenditureCategory) {
            this.expenditureCategory = expenditureCategory;
        }

        public String getExpenditureAmount() {
            return expenditureAmount;
        }

        public void setExpenditureAmount(String expenditureAmount) {
            this.expenditureAmount = expenditureAmount;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }

    /**
     * The helper class to build StatementTableModel given the statement data.
     */
    public static class StatementTableModelBuilder {

        private String id;
        private String reportDate;
        private List<Income> incomes;
        private List<Expenditure> expenditures;

        public StatementTableModelBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public StatementTableModelBuilder withReportDate(String reportDate) {
            this.reportDate = reportDate;
            return this;
        }

        public StatementTableModelBuilder withIncomes(List<Income> incomes) {
            this.incomes = incomes;
            return this;
        }

        public StatementTableModelBuilder withExpenditures(List<Expenditure> expenditures) {
            this.expenditures = expenditures;
            return this;
        }

        public StatementTableModel build() {
            StatementTableModel statementModel = new StatementTableModel();
            statementModel.setId(this.id);
            statementModel.setReportDate(this.reportDate);
            statementModel.setRows(new ArrayList<>());

            for (int i = 0; i < incomes.size() || i < expenditures.size(); i++) {
                StatementTableRowModel row = new StatementTableRowModel();
                if (incomes == null || i >= incomes.size()) {
                    row.setIncomeCategory("");
                    row.setIncomeAmount("");
                } else {
                    row.setIncomeCategory(incomes.get(i).getCategory());
                    row.setIncomeAmount(incomes.get(i).getAmount().toString() );
                }

                if (expenditures == null || i >= expenditures.size()) {
                    row.setExpenditureCategory("");
                    row.setExpenditureAmount("");
                } else {
                    row.setExpenditureCategory(expenditures.get(i).getCategory());
                    row.setExpenditureAmount(expenditures.get(i).getAmount().toString());
                }
                statementModel.addRow(row);
            }
            return statementModel;
        }
    }

}
