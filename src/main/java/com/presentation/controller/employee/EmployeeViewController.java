package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.SaleService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.presentation.animation.AnimationEngine.fadeNodeIn;
import static com.presentation.concurrency.ConcurrencyManager.executeUITask;
import static com.presentation.animation.AnimationEngineConstants.ANIMATION_DELAY_IN_MS;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_EMPLOYEE_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.*;
import static com.presentation.constants.ViewPath.*;
import static com.presentation.support.control.ComboBoxHelper.cleanComboBoxes;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.view.ContainerManager.*;
import static com.presentation.support.view.FXMLViewLoader.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;

@Component
@RequiredArgsConstructor
public class EmployeeViewController {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;
    private final SaleService saleService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label
            currently_active_employees,
            total_registered_employees,
            highest_revenue_employee_name,
            highest_revenue_value,
            highest_amount_of_services_completed_employee,
            amount_of_services_completed,
            average_completed_services_by_active_employees,
            results_count;

    @FXML
    private TextField search_field;

    @FXML
    private ComboBox<EmployeeStatus> status_filter;

    @FXML
    private ComboBox<HireDateRange> hire_date_filter;

    @FXML
    private MFXButton
            clear_filters_button,
            new_employee_button;

    @FXML
    private VBox employee_list_container;

    @FXML
    public void initialize() {

        List<EmployeeInfoDTO> employees = employeeService.getEmployeeList();

        loadActiveEmployeesStats();
        loadEmployeeRevenueStats();
        loadEmployeeCompletedServicesStats();
        loadProductivityStats();

        setTextOnLabel(results_count, parseNumberValueToText(employeeService.getEmployeeCount()));

        loadEnumsOnComboBox(status_filter, EmployeeStatus.values());
        loadEnumsOnComboBox(hire_date_filter, HireDateRange.values());

        setStringConverter(status_filter, EmployeeStatus.TODOS);
        setStringConverter(hire_date_filter, HireDateRange.TODOS);

        configureLiveSearch();
        configureButtonActions();

        loadEmployeeListOnView(employees);
    }

    private void loadActiveEmployeesStats() {

        executeUITask(
                employeeService::getActiveEmployees,
                activeEmployeesAmount -> {
                    setTextOnLabel(currently_active_employees, parseNumberValueToText(activeEmployeesAmount));
                    setTextOnLabel(total_registered_employees, "De " + parseNumberValueToText(employeeService.getEmployeeCount()) + " en total");
                }
        );
    }

    private void loadEmployeeRevenueStats() {

        executeUITask(
                saleService::getEmployeeWithHighestRevenue,
                employeeRevenueDTO -> {
                    setTextOnLabel(highest_revenue_employee_name, employeeRevenueDTO.getEmployeeFirstname() + "\n" + employeeRevenueDTO.getEmployeeLastname());
                    setTextOnLabel(highest_revenue_value, CURRENCY_STRING_ARG + parseNumberValueToText(employeeRevenueDTO.getTotalRevenue()));
                }
        );
    }

    private void loadEmployeeCompletedServicesStats() {

        executeUITask(
                saleService::getEmployeeWithMostServicesCompleted,
                employeeCompletedServicesDTO -> {
                    setTextOnLabel(highest_amount_of_services_completed_employee, employeeCompletedServicesDTO.getEmployeFirstName() + "\n" + employeeCompletedServicesDTO.getEmployeLastName());
                    setTextOnLabel(amount_of_services_completed, parseNumberValueToText(employeeCompletedServicesDTO.getTotalServices()));
                }
        );
    }

    private void loadProductivityStats() {

        executeUITask(
                saleService::getActiveEmployeesAverageServices,
                averageValue -> setTextOnLabel(average_completed_services_by_active_employees, formatAsDecimalValue(averageValue))
        );
    }

    private void loadEmployeeListOnView(List<EmployeeInfoDTO> employees) {

        if (employees.isEmpty()) {

            showEmptyListLabel(EMPTY_EMPLOYEE_LIST_MESSAGE, employee_list_container);

        } else {

            for (int i = 0; i < employees.size(); i++) {

                EmployeeInfoDTO infoDTO = employees.get(i);

                FXMLLoader loader = generateLoaderWithPath(EMPLOYEE_ITEM_VIEW_PATH);

                Parent employeeItem = returnParentFromLoader(loader, EMPLOYEE_ITEM_VIEW_LOADING_FAILED);

                EmployeeItemController employeeItemController = loader.getController();

                employeeItemController.setDataOnItem(infoDTO);

                employeeItemController.setOnEditCallBack(this::goToEditEmployeeView);
                employeeItemController.setOnStatusChangeCallBack(this::changeEmployeeStatus);

                loadItemOnVBox(employee_list_container, employeeItem);

                fadeNodeIn(employee_list_container, i * ANIMATION_DELAY_IN_MS);
            }
        }
    }

    private void goToEditEmployeeView(EmployeeInfoDTO infoDTO) {

        FXMLLoader loader = generateLoaderWithPath(EMPLOYEE_EDITION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent employeeUpdateView = returnParentFromLoader(loader, EMPLOYEE_EDITION_VIEW_LOADING_FAILED);

        EmployeeEditionController employeeEditionController = loader.getController();

        employeeEditionController.initialize(infoDTO);

        setViewOnPaneCenter(anchor_pane, employeeUpdateView);
    }

    private void goToRegisterNewEmployeeView() {

        loadViewOnPane(EMPLOYEE_CREATION_VIEW_PATH, applicationContext, EMPLOYEE_CREATION_VIEW_LOADING_FAILED, anchor_pane);
    }

    private void changeEmployeeStatus(EmployeeInfoDTO infoDTO) {

        employeeService.changeEmployeeIsActiveValue(infoDTO.getId());
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                clear_filters_button, this::cleanFiltersAndLiveSearch,
                new_employee_button, this::goToRegisterNewEmployeeView
        );

        configureRunnableMaps(map);
    }

    private void configureLiveSearch() {

        search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        status_filter.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        hire_date_filter.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    private void executeLiveSearch() {

        String employeeName = search_field.getText();

        EmployeeStatus selectedStatus = status_filter.getValue();

        if (selectedStatus == EmployeeStatus.TODOS) {

            selectedStatus = null;
        }

        HireDateRange selectedDateRange = hire_date_filter.getValue();

        List<EmployeeInfoDTO> employees = employeeService.liveSearch(employeeName, selectedStatus, selectedDateRange);

        cleanContainer(employee_list_container);

        loadEmployeeListOnView(employees);
    }

    private void cleanFiltersAndLiveSearch() {

        setBlankTextfield(search_field);

        cleanComboBoxes(status_filter, hire_date_filter);
    }
}