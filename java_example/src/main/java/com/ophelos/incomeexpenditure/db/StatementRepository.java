package com.ophelos.incomeexpenditure.db;

import com.ophelos.incomeexpenditure.db.entities.StatementEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for StatementEntity.
 */
public interface StatementRepository extends CrudRepository<StatementEntity, Long> {

    public List<StatementEntity> findByUserId(Long userId);

    public StatementEntity findByIdAndUserId(Long id, Long userId);

}
