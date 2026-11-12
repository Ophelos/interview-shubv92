package com.ophelos.incomeexpenditure.db;

import com.ophelos.incomeexpenditure.db.entities.IncomeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for IncomeEntity.
 */
public interface IncomeRepository extends CrudRepository<IncomeEntity, Long> {

    public List<IncomeEntity> findByStatementId(Long statementId);

}
