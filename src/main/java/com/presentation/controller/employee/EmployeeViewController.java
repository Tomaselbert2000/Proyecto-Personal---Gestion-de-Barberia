package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.SaleService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
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

import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
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

        executeAsyncTask(
                employeeService::getActiveEmployees,
                activeEmployeesAmount -> {
                    setTextOnLabel(currently_active_employees, parseNumberValueToText(activeEmployeesAmount));
                    setTextOnLabel(total_registered_employees, "De " + parseNumberValueToText(employeeService.getEmployeeCount()) + " en total");
                }
        );
    }

    private void loadEmployeeRevenueStats() {

        executeAsyncTask(
                saleService::getEmployeeWithHighestRevenue,
                employeeRevenueDTO -> {
                    setTextOnLabel(highest_revenue_employee_name, employeeRevenueDTO.getEmployeeFirstname() + "\n" + employeeRevenueDTO.getEmployeeLastname());
                    setTextOnLabel(highest_revenue_value, CURRENCY_STRING_ARG + parseNumberValueToText(employeeRevenueDTO.getTotalRevenue()));
                }
        );
    }

    private void loadEmployeeCompletedServicesStats() {

        executeAsyncTask(
                saleService::getEmployeeWithMostServicesCompleted,
                employeeCompletedServicesDTO -> {
                    setTextOnLabel(highest_amount_of_services_completed_employee, employeeCompletedServicesDTO.getEmployeFirstName() + "\n" + employeeCompletedServicesDTO.getEmployeLastName());
                    setTextOnLabel(amount_of_services_completed, parseNumberValueToText(employeeCompletedServicesDTO.getTotalServices()));
                }
        );
    }

    private void loadProductivityStats() {

        executeAsyncTask(
                saleService::getActiveEmployeesAverageServices,
                averageValue -> setTextOnLabel(average_completed_services_by_active_employees, formatAsDecimalValue(averageValue))
        );
    }

    private void loadEmployeeListOnView(List<EmployeeInfoDTO> employeeList) {

        loadItemsOnController(
                employeeList,
                employee_list_container,
                EmployeeItemController.class,
                EMPLOYEE_ITEM_VIEW_PATH,
                EMPTY_EMPLOYEE_LIST_MESSAGE,
                EMPLOYEE_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnEditCallBack(this::goToEditEmployeeView);
                    itemController.setOnStatusChangeCallBack(this::changeEmployeeStatus);
                }
        );
    }

    private void goToEditEmployeeView(EmployeeInfoDTO infoDTO) {

        loadViewWithControllerPane(
                EMPLOYEE_EDITION_VIEW_PATH,
                applicationContext,
                EMPLOYEE_EDITION_VIEW_LOADING_FAILED,
                anchor_pane,
                EmployeeEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
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