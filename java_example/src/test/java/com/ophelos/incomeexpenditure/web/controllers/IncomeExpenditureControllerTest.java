package com.ophelos.incomeexpenditure.web.controllers;

import com.ophelos.incomeexpenditure.api.Expenditure;
import com.ophelos.incomeexpenditure.api.Income;
import com.ophelos.incomeexpenditure.api.Statement;
import com.ophelos.incomeexpenditure.services.IncomeExpenditureService;
import com.ophelos.incomeexpenditure.web.data.ExpenditureImpl;
import com.ophelos.incomeexpenditure.web.data.IncomeImpl;
import com.ophelos.incomeexpenditure.web.data.StatementImpl;
import com.ophelos.incomeexpenditure.web.models.StatementTableModel;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncomeExpenditureController.class)
public class IncomeExpenditureControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private IncomeExpenditureService incomeExpenditureService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        when(incomeExpenditureService.findStatementByIdAndUsername(Long.valueOf(1L), "user"))
                .thenReturn(new StatementImpl(Long.valueOf(1L), Long.valueOf(1L), "2024/07/28"));
    }

    private UserDetails userDetails = User.withUsername("user")
            .password("p@ssw0rd!")
            .roles("USER")
            .build();

    @Test
    public void testHomePage() throws Exception {
        this.mvc.perform(get("/").with(user(userDetails))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("What would you like to do?")));
    }

    @Test
    public void testStatementPageWithValidStatementId_Empty() throws Exception {
        this.mvc.perform(get("/statement?id=1").with(user(userDetails))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("No income or expenditure for this statement")));
    }

    @Test
    public void testStatementPageWithValidStatementId_NonEmpty() throws Exception {

        // Setup some test income and expenditure data

        ArrayList<Income> incomes = new ArrayList<>();
        incomes.add(new IncomeImpl("Salary", 2800));
        incomes.add(new IncomeImpl("Other", 300));

        ArrayList<Expenditure> expenditures = new ArrayList<>();
        expenditures.add(new ExpenditureImpl("Mortgage", 600));
        expenditures.add(new ExpenditureImpl("Utilities", 100));
        expenditures.add(new ExpenditureImpl("Travel", 150));
        expenditures.add(new ExpenditureImpl("Food", 500));
        expenditures.add(new ExpenditureImpl("Loan Repayment", 1000));

        when(incomeExpenditureService.findIncomesByStatementId(1L)).thenReturn(incomes);
        when(incomeExpenditureService.findExpendituresByStatementId(1L)).thenReturn(expenditures);

        this.mvc.perform(get("/statement?id=1").with(user(userDetails))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Salary")))
                .andExpect(content().string(containsString("2800")))
                .andExpect(content().string(containsString("Other")))
                .andExpect(content().string(containsString("300")))
                .andExpect(content().string(containsString("Mortgage")))
                .andExpect(content().string(containsString("600")))
                .andExpect(content().string(containsString("Utilities")))
                .andExpect(content().string(containsString("100")))
                .andExpect(content().string(containsString("Travel")))
                .andExpect(content().string(containsString("150")))
                .andExpect(content().string(containsString("Food")))
                .andExpect(content().string(containsString("500")))
                .andExpect(content().string(containsString("Loan Repayment")))
                .andExpect(content().string(containsString("1000")));
    }

    @Test
    public void testStatementPageWithInvalidStatementId() throws Exception {
        this.mvc.perform(get("/statement?id=2").with(user(userDetails))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Cannot find statement")));
    }

    @Test
    public void testNewStatementPage() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.post("/new").with(user(userDetails)).with(csrf());

        this.mvc.perform(request).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("rows[0].incomeCategory")))
                .andExpect(content().string(containsString("rows[1].incomeCategory")))
                .andExpect(content().string(containsString("rows[2].incomeCategory")))
                .andExpect(content().string(containsString("rows[3].incomeCategory")))
                .andExpect(content().string(containsString("rows[4].incomeCategory")));
    }

    @Test
    public void testSaveStatement() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post("/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("rows[0].incomeCategory", "Salary"),
                        new BasicNameValuePair("rows[0].incomeAmount", "2800"),
                        new BasicNameValuePair("rows[0].expenditureCategory", ""),
                        new BasicNameValuePair(".expenditureAmount", "")))))
                .with(user(userDetails))
                .with(csrf());


        when(incomeExpenditureService.saveStatement(anyString(), anyString())).thenReturn(new StatementImpl(1L, 1L, "2024/07/28"));

        this.mvc.perform(request).andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(header().exists(LOCATION))
                .andExpect(header().string(LOCATION, containsString("/statement")));
    }

    @Test
    public void testSaveStatement_InvalidData() throws Exception {

        StatementTableModel reqData = new StatementTableModel();
        reqData.addRow("Salary", "-2800", "", "");

        RequestBuilder request = MockMvcRequestBuilders.post("/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("rows[0].incomeCategory", "Salary"),
                        new BasicNameValuePair("rows[0].incomeAmount", "-2800"),
                        new BasicNameValuePair("rows[0].expenditureCategory", ""),
                        new BasicNameValuePair(".expenditureAmount", "")))))
                .with(user(userDetails))
                .with(csrf());

        this.mvc.perform(request).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("cannot be negative")));
    }

}
