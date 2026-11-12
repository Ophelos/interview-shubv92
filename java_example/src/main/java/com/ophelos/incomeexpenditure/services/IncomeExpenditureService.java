package com.ophelos.incomeexpenditure.services;

import com.ophelos.incomeexpenditure.api.Expenditure;
import com.ophelos.incomeexpenditure.api.Income;
import com.ophelos.incomeexpenditure.api.Statement;
import com.ophelos.incomeexpenditure.api.User;
import com.ophelos.incomeexpenditure.db.ExpenditureRepository;
import com.ophelos.incomeexpenditure.db.IncomeRepository;
import com.ophelos.incomeexpenditure.db.StatementRepository;
import com.ophelos.incomeexpenditure.db.UserRepository;
import com.ophelos.incomeexpenditure.db.entities.ExpenditureEntity;
import com.ophelos.incomeexpenditure.db.entities.IncomeEntity;
import com.ophelos.incomeexpenditure.db.entities.StatementEntity;
import com.ophelos.incomeexpenditure.db.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling statement, income and expenditure data, separating the controller from the database layer.
 */
public class IncomeExpenditureService {

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    IncomeRepository incomeRepository;

    @Autowired
    ExpenditureRepository expenditureRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Statement findStatementById(Long id) {
        return statementRepository.findById(id).orElse(null);
    }

    public Statement findStatementByIdAndUsername(Long id, String username) {
        UserEntity user = userRepository.findByUsername(username);
        return statementRepository.findByIdAndUserId(id, user.getId());
    }

    public List<Income> findIncomesByStatementId(Long statementId) {
        ArrayList<Income> incomes = new ArrayList<>();
        incomeRepository.findByStatementId(statementId).forEach(
                entity -> incomes.add((Income) entity));
        return incomes;
    }

    public List<Expenditure> findExpendituresByStatementId(Long statementId) {
        ArrayList<Expenditure> expenditures = new ArrayList<>();
        expenditureRepository.findByStatementId(statementId).forEach(
                entity -> expenditures.add((Expenditure) entity));
        return expenditures;
    }

    public Statement saveStatement(String username, String reportedDate) {
        UserEntity user = userRepository.findByUsername(username);
        StatementEntity statementEntity = new StatementEntity();
        statementEntity.setUserId(user.getId());
        statementEntity.setReportedDate(reportedDate);
        statementRepository.save(statementEntity);
        return statementEntity;
    }

    public Income saveIncomeToStatement(Statement statement, String category, Integer amount) {
        IncomeEntity incomeEntity = new IncomeEntity();
        incomeEntity.setStatementId(statement.getId());
        incomeEntity.setCategory(category);
        incomeEntity.setAmount(amount);
        incomeRepository.save(incomeEntity);
        return incomeEntity;
    }

    public Expenditure saveExpenditureToStatement(Statement statement, String category, Integer amount) {
        ExpenditureEntity expenditureEntity = new ExpenditureEntity();
        expenditureEntity.setStatementId(statement.getId());
        expenditureEntity.setCategory(category);
        expenditureEntity.setAmount(amount);
        expenditureRepository.save(expenditureEntity);
        return expenditureEntity;
    }

}
