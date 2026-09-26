package com.presentation.controller.web;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.presentation.controller.BaseWebController;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_EMPLOYEES;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class WebEmployeeController extends BaseWebController<EmployeeCreationDTO> {

    private final EmployeeService employeeService;
    private final SaleService saleService;

    @GetMapping
    public String showEmployeeCatalog(
            Model model,
            Principal principal,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) HireDateRange hireDateRange
    ) {

        model.addAttribute("currentUser", principal.getName());

        model.addAttribute("employeeStatuses", EmployeeStatus.values());
        model.addAttribute("hireDateRangeValues", HireDateRange.values());

        model.addAttribute("activeEmployees", employeeService.getActiveEmployees());
        model.addAttribute("totalEmployees", employeeService.getEmployeeCount());
        model.addAttribute("employeeRevenueStats", saleService.getEmployeeWithHighestRevenue());
        model.addAttribute("employeeCompletedServicesStats", saleService.getEmployeeWithMostServicesCompleted());
        model.addAttribute("averageByEmployee", saleService.getActiveEmployeesAverageServices());

        model.addAttribute("liveSearch", employeeService.liveSearch(employeeName, status, hireDateRange));

        model.addAttribute("employeeName", employeeName);
        model.addAttribute("status", status);
        model.addAttribute("hireDateRange", hireDateRange);

        return EMPLOYEES;
    }

    @GetMapping("/new")
    public String showEmployeeCreationForm(
            Model model,
            Principal principal
    ) {

        return showCreationForm(model, principal, new EmployeeCreationDTO());
    }

    @PostMapping("/new")
    public String createNewEmployee(
            @Valid @ModelAttribute EmployeeCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return createEntity(dto, bindingResult, model, principal, REDIRECT_EMPLOYEES);
    }

    @PostMapping("/{employeeID}/delete")
    public String deleteEmployee(@PathVariable Long employeeID) {

        employeeService.deleteEmployee(employeeID);

        return REDIRECT_EMPLOYEES;
    }

    @GetMapping("/{employeeID}/update")
    public String updateEmployee(@PathVariable Long employeeID, Model model) {

        model.addAttribute("dto", employeeService.getEmployeeInfo(employeeID));

        return EMPLOYEE_UPDATE;
    }

    @PostMapping("/{employeeID}/update")
    public String updateEmployee(@PathVariable Long employeeID, @ModelAttribute EmployeeUpdateDTO dto) {

        employeeService.updateEmployee(employeeID, dto);

        return redirectToUpdate(REDIRECT_EMPLOYEES, employeeID);
    }

    @PostMapping("/{employeeID}/toggleActivityStatus")
    public String toggleActivityStatus(@PathVariable Long employeeID) {

        employeeService.changeEmployeeIsActiveValue(employeeID);

        return REDIRECT_EMPLOYEES;
    }

    @Override
    protected void executeCreation(EmployeeCreationDTO dto) {

        employeeService.registerNewEmployee(dto);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, EmployeeCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationForm(model);

        return EMPLOYEE_CREATION;
    }

    @Override
    protected void populateCreationForm(Model model) {
        // Intentionally empty. Employee CRUD does not rely on other's entity information
    }
}
