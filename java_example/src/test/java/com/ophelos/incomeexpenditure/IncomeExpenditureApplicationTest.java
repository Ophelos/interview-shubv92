package com.ophelos.incomeexpenditure;

import static org.assertj.core.api.Assertions.assertThat;

import com.ophelos.incomeexpenditure.services.IncomeExpenditureService;
import com.ophelos.incomeexpenditure.web.controllers.IncomeExpenditureController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IncomeExpenditureApplicationTest {

	@Autowired
	IncomeExpenditureService incomeExpenditureService;

	@Autowired
	IncomeExpenditureController incomeExpenditureController;

	@Test
	void contextLoads() {
		assertThat(incomeExpenditureService).isNotNull();
		assertThat(incomeExpenditureController).isNotNull();
	}

}
