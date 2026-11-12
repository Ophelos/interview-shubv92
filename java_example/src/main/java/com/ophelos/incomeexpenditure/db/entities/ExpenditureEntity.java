package com.ophelos.incomeexpenditure.db.entities;

import com.ophelos.incomeexpenditure.api.Expenditure;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

@Entity
@Table(name = "expenditure")
public class ExpenditureEntity implements Serializable, Expenditure {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // Identifies the statement this expenditure belongs to
    @Column(name = "statement_id", nullable = false)
    private Long statementId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatementId() {
        return statementId;
    }

    public void setStatementId(Long statementId) {
        this.statementId = statementId;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
