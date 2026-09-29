package com.presentation.controller.web;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.exceptions.BusinessException;
import com.mapper.interfaces.EmployeeMapper;
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
import static com.presentation.constants.StringResource.OperationMessages.EMPLOYEE_OPERATION_FAILED;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class WebEmployeeController extends BaseWebController<EmployeeCreationDTO, EmployeeUpdateDTO> {

    private static final String OPERATION_FAILED_TAG = "operationFailedMessage";

    private final EmployeeService employeeService;
    private final SaleService saleService;
    private final EmployeeMapper mapper;

    @GetMapping
    public String showEmployeeCatalog(
            Model model,
            Principal principal,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String hireDateRange
    ) {

        model.addAttribute("currentUser", principal.getName());

        model.addAttribute("employeeStatuses", EmployeeStatus.values());
        model.addAttribute("hireDateRangeValues", HireDateRange.values());

        model.addAttribute("activeEmployees", employeeService.getActiveEmployees());
        model.addAttribute("totalEmployees", employeeService.getEmployeeCount());
        model.addAttribute("employeeRevenueStats", saleService.getEmployeeWithHighestRevenue());
        model.addAttribute("employeeCompletedServicesStats", saleService.getEmployeeWithMostServicesCompleted());
        model.addAttribute("averageByEmployee", saleService.getActiveEmployeesAverageServices());

        model.addAttribute("liveSearch", employeeService.liveSearch(employeeName, parseEnumValue(status, EmployeeStatus.class), parseEnumValue(hireDateRange, HireDateRange.class)));

        model.addAttribute("employeeName", employeeName);
        model.addAttribute("status", status);
        model.addAttribute("hireDateRange", hireDateRange);

        model.addAttribute(OPERATION_FAILED_TAG, EMPLOYEE_OPERATION_FAILED);

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

        return deleteEntity(employeeID, REDIRECT_EMPLOYEES);
    }

    @GetMapping("/{employeeID}/update")
    public String updateEmployee(
            @PathVariable Long employeeID,
            Model model,
            Principal principal
    ) {

        try {

            return renderUpdateForm(model, employeeID, principal, invokeServiceAndReturnDTO(employeeID));

        } catch (BusinessException e) {

            return REDIRECT_EMPLOYEES + ERROR_SUFFIX;
        }
    }

    @PostMapping("/{employeeID}/update")
    public String updateEmployee(
            @PathVariable Long employeeID,
            @Valid @ModelAttribute EmployeeUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return updateEntity(employeeID, dto, bindingResult, model, principal, REDIRECT_EMPLOYEES);
    }

    @PostMapping("/{employeeID}/toggleActivityStatus")
    public String toggleActivityStatus(
            @PathVariable Long employeeID
    ) {

        try {

            employeeService.changeEmployeeIsActiveValue(employeeID);

            return REDIRECT_EMPLOYEES;

        } catch (BusinessException exception) {

            return REDIRECT_EMPLOYEES + ERROR_SUFFIX;
        }
    }

    @Override
    protected EmployeeUpdateDTO invokeServiceAndReturnDTO(Long id) {

        return mapper.mapInfoDTOtoUpdateDTO(employeeService.getEmployeeInfo(id));
    }

    @Override
    protected void executeCreation(EmployeeCreationDTO dto) {

        employeeService.registerNewEmployee(dto);
    }

    @Override
    protected void executeUpdate(Long entityID, EmployeeUpdateDTO dto) {

        employeeService.updateEmployee(entityID, dto);
    }

    @Override
    protected void executeDeletion(Long entityID) {

        employeeService.deleteEmployee(entityID);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, EmployeeCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationForm(model);

        return EMPLOYEE_CREATION;
    }

    @Override
    protected String renderUpdateForm(Model model, Long entityID, Principal principal, EmployeeUpdateDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("employee", employeeService.getEmployeeInfo(entityID));
        model.addAttribute("dto", dto);

        return EMPLOYEE_UPDATE;
    }

    @Override
    protected void populateCreationForm(Model model) {
        // Intentionally empty. Employee CRUD does not rely on other's entity information
    }

    /**
     * Resuelve un valor de enumerado recibido por query string de forma tolerante: los valores
     * ausentes, vacíos o no reconocidos se interpretan como "sin filtro" en lugar de provocar
     * un error 400.
     */
    private <E extends Enum<E>> E parseEnumValue(String rawValue, Class<E> enumType) {

        if (rawValue == null || rawValue.isBlank()) return null;

        try {

            return Enum.valueOf(enumType, rawValue.trim());

        } catch (IllegalArgumentException exception) {

            return null;
        }
    }
}
