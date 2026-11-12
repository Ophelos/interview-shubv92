package com.ophelos.incomeexpenditure.db;

import com.ophelos.incomeexpenditure.db.entities.ExpenditureEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for ExpenditureEntity.
 */
public interface ExpenditureRepository extends CrudRepository<ExpenditureEntity, Long> {

    /**
     * Find all the expenditures for the given statement.
     *
     * @param statementId ID of the statement
     * @return List of expenditure entities
     */
    public List<ExpenditureEntity> findByStatementId(Long statementId);

}
