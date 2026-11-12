package com.ophelos.incomeexpenditure.db;

import com.ophelos.incomeexpenditure.db.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for UserEntity.
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    public UserEntity findByUsername(String username);

}
