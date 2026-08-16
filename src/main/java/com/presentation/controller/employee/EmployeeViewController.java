package com.presentation.controller.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.presentation.controller.BaseCatalogViewController;
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
import static com.presentation.support.control.ComboBoxHelper.*;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.*;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewOnPane;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;

@Component
@RequiredArgsConstructor
public class EmployeeViewController extends BaseCatalogViewController<EmployeeInfoDTO> {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;
    private final SaleService saleService;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label
            currentlyActiveEmployees,
            totalRegisteredEmployees,
            highestRevenueEmployeeName,
            highestRevenueValue,
            highestServicesCompletedEmployeeName,
            completedServicesCount,
            averageCompletedServicesByActiveEmployees,
            resultsCount;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<EmployeeStatus> statusFilter;

    @FXML
    private ComboBox<HireDateRange> hireDateFilter;

    @FXML
    private MFXButton
            clearFiltersButton,
            newEmployeeButton;

    @FXML
    private VBox employeeListContainer;

    @FXML
    public void initialize() {

        loadGlobalStats();

        initializeListContent();

        attachLiveSearchListeners(
                searchField.textProperty(),
                statusFilter.valueProperty(),
                hireDateFilter.valueProperty()
        );

        configureButtonActions();
    }

    private void loadActiveEmployeesStats() {

        executeAsyncTask(
                employeeService::getActiveEmployees,
                activeEmployeesAmount -> {
                    setTextOnLabel(currentlyActiveEmployees, parseNumberValueToText(activeEmployeesAmount));
                    setTextOnLabel(totalRegisteredEmployees, "De " + parseNumberValueToText(employeeService.getEmployeeCount()) + " en total");
                }
        );
    }

    private void loadEmployeeRevenueStats() {

        executeAsyncTask(
                saleService::getEmployeeWithHighestRevenue,
                employeeRevenueDTO -> {
                    setTextOnLabel(highestRevenueEmployeeName, employeeRevenueDTO.getEmployeeFirstname() + "\n" + employeeRevenueDTO.getEmployeeLastname());
                    setTextOnLabel(highestRevenueValue, CURRENCY_STRING_ARG + parseNumberValueToText(employeeRevenueDTO.getTotalRevenue()));
                }
        );
    }

    private void loadEmployeeCompletedServicesStats() {

        executeAsyncTask(
                saleService::getEmployeeWithMostServicesCompleted,
                employeeCompletedServicesDTO -> {
                    setTextOnLabel(highestServicesCompletedEmployeeName, employeeCompletedServicesDTO.getEmployeFirstName() + "\n" + employeeCompletedServicesDTO.getEmployeLastName());
                    setTextOnLabel(completedServicesCount, parseNumberValueToText(employeeCompletedServicesDTO.getTotalServices()));
                }
        );
    }

    private void loadProductivityStats() {

        executeAsyncTask(
                saleService::getActiveEmployeesAverageServices,
                averageValue -> setTextOnLabel(averageCompletedServicesByActiveEmployees, formatAsDecimalValue(averageValue))
        );
    }

    private void goToEditEmployeeView(EmployeeInfoDTO infoDTO) {

        loadViewWithControllerPane(
                EMPLOYEE_EDITION_VIEW_PATH,
                applicationContext,
                EMPLOYEE_EDITION_VIEW_LOADING_FAILED,
                anchorPane,
                EmployeeEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }

    private void goToRegisterNewEmployeeView() {

        loadViewOnPane(EMPLOYEE_CREATION_VIEW_PATH, applicationContext, EMPLOYEE_CREATION_VIEW_LOADING_FAILED, anchorPane);
    }

    private void changeEmployeeStatus(EmployeeInfoDTO infoDTO) {

        employeeService.changeEmployeeIsActiveValue(infoDTO.getId());
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                clearFiltersButton, this::resetSearchFilter,
                newEmployeeButton, this::goToRegisterNewEmployeeView
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void loadGlobalStats() {

        loadActiveEmployeesStats();
        loadEmployeeRevenueStats();
        loadEmployeeCompletedServicesStats();
        loadProductivityStats();
    }

    @Override
    protected Label getResultsCountLabel() {

        return resultsCount;
    }

    @Override
    protected List<EmployeeInfoDTO> searchCatalog() {

        String employeeName = searchField.getText();

        EmployeeStatus selectedStatus = statusFilter.getValue();

        if (selectedStatus == EmployeeStatus.TODOS) {

            selectedStatus = null;
        }

        HireDateRange selectedDateRange = hireDateFilter.getValue();

        return employeeService.liveSearch(employeeName, selectedStatus, selectedDateRange);
    }

    @Override
    protected VBox getItemListContainer() {

        return employeeListContainer;
    }

    @Override
    protected void loadItemsOnView(List<EmployeeInfoDTO> items) {

        loadItemsOnController(
                items,
                employeeListContainer,
                EmployeeItemController.class,
                EMPLOYEE_ITEM_VIEW_PATH,
                EMPTY_EMPLOYEE_LIST_MESSAGE,
                EMPLOYEE_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnEditCallback(this::goToEditEmployeeView);
                    itemController.setOnStatusChangeCallback(this::changeEmployeeStatus);
                }
        );
    }

    @Override
    protected void clearFilterNodes() {

        setBlankTextfield(searchField);
        resetComboBoxFilter(statusFilter, hireDateFilter);
    }

    @Override
    protected void initializeListContent() {

        List<EmployeeInfoDTO> employees = employeeService.getEmployeeList();

        loadItemsOnView(employees);

        setTextOnLabel(resultsCount, parseNumberValueToText(employeeService.getEmployeeCount()));

        loadEnumsOnComboBox(statusFilter, EmployeeStatus.values());
        loadEnumsOnComboBox(hireDateFilter, HireDateRange.values());

        setStringConverter(statusFilter, EmployeeStatus.TODOS);
        setStringConverter(hireDateFilter, HireDateRange.TODOS);
    }
}