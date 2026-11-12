package com.ophelos.incomeexpenditure.web.controllers;

import com.ophelos.incomeexpenditure.api.Expenditure;
import com.ophelos.incomeexpenditure.api.Income;
import com.ophelos.incomeexpenditure.api.Statement;
import com.ophelos.incomeexpenditure.services.IncomeExpenditureService;
import com.ophelos.incomeexpenditure.web.models.StatementTableModel;
import com.ophelos.incomeexpenditure.web.models.StatementTableModel.StatementTableRowModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The controller to process incoming requests from the HTTP client.
 */
@Controller
public class IncomeExpenditureController {

    private static final int INCREMENT = 5;

    // Service for saving and retrieving statement data
    @Autowired
    private IncomeExpenditureService incomeExpenditureService;

    // Validator for validating statement data.
    private StatementDataValidator statementDataValidator = new StatementDataValidator();

    // Date formatting for display
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd");

    /**
     * Home page
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(Model model) {
        return "index.html";
    }

    /**
     * Show requested statement.
     *
     * @param statementId
     * @param model
     * @return
     */
    @GetMapping("/statement")
    public String showStatement(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "id", required = false) String statementId, Model model) {
        if (statementId == null || statementId.isEmpty()) {
            return "index.html";
        }

        Statement statement = incomeExpenditureService.findStatementByIdAndUsername(Long.valueOf(statementId), userDetails.getUsername());
        if (statement == null) {
            model.addAttribute("error", "Cannot find statement.");
            return "index.html";
        }

        List<Income> incomeItems = incomeExpenditureService.findIncomesByStatementId(Long.valueOf(statementId));
        List<Expenditure> expenditureItems = incomeExpenditureService.findExpendituresByStatementId(Long.valueOf(statementId));

        StatementTableModel statementModel = StatementTableModel.withId(statementId)
                .withReportDate(statement.getReportedDate())
                .withIncomes(incomeItems)
                .withExpenditures(expenditureItems)
                .build();

        model.addAttribute("statement", statementModel);

        return "statement";
    }

    /**
     * Show a blank form so user can enter income & expense data and create a new statement.
     *
     * @param addRows
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/new")
    public String newStatement(@RequestParam(name = "add", defaultValue = "true") boolean addRows,
                               @ModelAttribute StatementTableModel form, Model model) {
        if (addRows) {
            for (int i = 0; i < INCREMENT; i++) {
                form.addEmptyRow();
            }
        }
        model.addAttribute("statement", form);
        return "newStatement";
    }

    /**
     * Create a new statement and save the income & expense data.
     *
     * @param user
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/save")
    @Transactional(rollbackOn = Exception.class)
    public String saveStatement(@AuthenticationPrincipal User user, @ModelAttribute StatementTableModel form, Model model) {
        List<StatementTableRowModel> rows = form.getRows();
        if (rows == null || rows.isEmpty()) {
            return "redirect:/";
        }

        rows.forEach((row) -> System.out.println("ROW ->" + row.toString()));

        // Validate the form data. Return to the form if invalid data is found.
        try {
            statementDataValidator.validate(form);

        } catch (DataValidationException e) {
            model.addAttribute("statement", form);
            model.addAttribute("error", e.getMessage());
            return "newStatement";
        }

        // Save the statement and all its income and expenditure items.
        Statement statement = incomeExpenditureService.saveStatement(user.getUsername(), dtf.format(LocalDate.now()));

        for (StatementTableRowModel row : rows) {
            if (row.getIncomeCategory() != null && !row.getIncomeCategory().trim().isEmpty() &&
                    row.getIncomeAmount() != null && !row.getIncomeAmount().trim().isEmpty()) {
                incomeExpenditureService.saveIncomeToStatement(statement, row.getIncomeCategory().trim(), Integer.valueOf(row.getIncomeAmount().trim()));
            }
            if (row.getExpenditureCategory() != null && !row.getExpenditureCategory().trim().isEmpty() &&
                    row.getExpenditureAmount() != null && !row.getExpenditureAmount().trim().isEmpty()) {
                incomeExpenditureService.saveExpenditureToStatement(statement, row.getExpenditureCategory().trim(), Integer.valueOf(row.getExpenditureAmount().trim()));
            }
        }

        return "redirect:/statement?id=" + statement.getId();
    }

    /**
     * Catch all unhandled errors. Show a generic error page.
     */
    @GetMapping("/error")
    public String error() {
        return "/error.html";
    }

}