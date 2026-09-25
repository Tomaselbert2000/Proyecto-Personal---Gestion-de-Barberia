package com.presentation.controller.web;

import com.dto.employee.EmployeeUpdateDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.EMPLOYEES;
import static com.presentation.constants.HtmlConstants.Paths.EMPLOYEE_UPDATE;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_EMPLOYEES;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class WebEmployeeController {

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
}
