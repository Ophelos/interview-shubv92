package com.ophelos.incomeexpenditure.web.data;

import com.ophelos.incomeexpenditure.api.Statement;

/**
 * Statement implementation class for testing.
 */
public class StatementImpl implements Statement {

    public Long id;
    public Long UserId;
    public String reportedDate;

    public StatementImpl(Long id, Long userId, String reportedDate) {
        this.id = id;
        this.UserId = userId;
        this.reportedDate = reportedDate;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getUserId() {
        return this.UserId;
    }

    @Override
    public String getReportedDate() {
        return this.reportedDate;
    }

}
