package com.ophelos.incomeexpenditure.db.entities;

import com.ophelos.incomeexpenditure.api.Statement;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "statement")
public class StatementEntity implements Serializable, Statement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // Identifies the user this statement belongs to
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reported_date", nullable = false)
    private String reportedDate;

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getReportedDate() {
        return this.reportedDate;
    }

    public void setReportedDate(String reportedDate) {
        this.reportedDate = reportedDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
