package com.barbershop.launcher.controller.implementation.employee;

import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.enums.EmployeeStatus;
import com.barbershop.enums.HireDateRange;
import com.barbershop.launcher.controller.interfaces.ViewController;
import com.barbershop.service.interfaces.EmployeeService;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.messages.EmptyListMessage.EMPTY_EMPLOYEE_LIST_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;

@Component
@RequiredArgsConstructor
public class EmployeeViewController implements ViewController {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label total_employees_count;

    @FXML
    private Label total_employees_trend;

    @FXML
    private Label active_employees_count;

    @FXML
    private Label active_employees_percentage;

    @FXML
    private Label inactive_employees_count;

    @FXML
    private Label inactive_employees_percentage;

    @FXML
    private Label average_productivity_count;

    @FXML
    private Label productivity_trend;

    @FXML
    private TextField search_field;

    @FXML
    private MFXComboBox<EmployeeStatus> status_filter;

    @FXML
    private MFXComboBox<HireDateRange> hire_date_filter;

    @FXML
    private Button clear_filters_button;

    @FXML
    private Button new_employee_button;

    @FXML
    private Label results_count;

    @FXML
    private VBox employee_list_container;

    @FXML
    public void initialize() {

        List<EmployeeInfoDTO> employees = employeeService.getEmployeeList();

        loadTotalEmployeesStats();
        loadEmployeeActivityStats(employees.size());
        loadProductivityStats();

        loadEnumsOnComboBox(status_filter, EmployeeStatus.values());
        loadEnumsOnComboBox(hire_date_filter, HireDateRange.values());

        setStringConverter(status_filter, EmployeeStatus.TODOS);
        setStringConverter(hire_date_filter, HireDateRange.TODOS);

        configureLiveSearch();
        configureButtonActions();

        loadEmployeeListOnView(employees);
    }

    private void loadTotalEmployeesStats() {

        Long count = employeeService.getEmployeeCount();
        Long employeesTrendCount = employeeService.getEmployeesTrendThisMonth();

        List<Label> labels = List.of(total_employees_count, total_employees_trend, results_count);
        List<String> texts = List.of(parseNumberValueToText(count), parseNumberValueToText(employeesTrendCount), parseNumberValueToText(count));

        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);
    }

    private void loadEmployeeActivityStats(int totalCount) {

        Long activeEmployeesCount = employeeService.getActiveEmployees();
        Long inactiveEmployeesCount = employeeService.getInactiveEmployees();

        double activePercentage;
        double inactivePercentage;

        if (totalCount > 0) {

            activePercentage = (activeEmployeesCount * 100.0) / totalCount;
            inactivePercentage = (inactiveEmployeesCount * 100.0) / totalCount;

        } else {

            activePercentage = 0.0;
            inactivePercentage = 0.0;
        }

        Map<Label, String> map = Map.of(
                active_employees_count, parseNumberValueToText(activeEmployeesCount),
                inactive_employees_count, parseNumberValueToText(inactiveEmployeesCount),
                active_employees_percentage, formatAsPercentage(activePercentage),
                inactive_employees_percentage, formatAsPercentage(inactivePercentage)
        );

        setTextsOnLabelMap(map);
    }

    private void loadProductivityStats() {

        double averageMonthlyProductivity = employeeService.calculateAverageMonthlyProductivity();
        double productivityTrend = employeeService.calculateProductivityTrendVsLastMonth();

        setTextOnLabel(average_productivity_count, formatAsDecimalValue(averageMonthlyProductivity));

        if (productivityTrend > 0.0) {

            setTextOnLabel(productivity_trend, "+" + formatAsPercentage(productivityTrend));

        } else {

            setTextOnLabel(productivity_trend, "-" + formatAsPercentage(productivityTrend));

        }
    }

    private void loadEmployeeListOnView(List<EmployeeInfoDTO> employees) {

        if (employees.isEmpty()) {

            showEmptyListLabel(EMPTY_EMPLOYEE_LIST_MESSAGE, employee_list_container);

        } else {

            for (EmployeeInfoDTO infoDTO : employees) {

                FXMLLoader loader = generateLoaderWithPath(EMPLOYEE_ITEM_VIEW_PATH);

                Parent employeeItem = returnParentFromLoader(loader, EMPLOYEE_ITEM_VIEW_LOADING_FAILED);

                EmployeeItemController employeeItemController = loader.getController();

                employeeItemController.setDataOnItem(infoDTO);

                employeeItemController.setOnEditCallBack(this::goToEditEmployeeView);
                employeeItemController.setOnStatusChangeCallBack(this::changeEmployeeStatus);

                loadItemOnVBox(employee_list_container, employeeItem);
            }
        }
    }

    private void goToEditEmployeeView(EmployeeInfoDTO infoDTO) {

        FXMLLoader loader = generateLoaderWithPath(EMPLOYEE_EDITION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent employeeUpdateView = returnParentFromLoader(loader, EMPLOYEE_EDITION_VIEW_LOADING_FAILED);

        EmployeeEditionController employeeEditionController = loader.getController();

        employeeEditionController.initialize(infoDTO);

        setViewOnAnchorPaneCenter(anchor_pane, employeeUpdateView);
    }

    private void goToRegisterNewEmployeeView() {

        loadViewOnPane(EMPLOYEE_CREATION_VIEW_PATH, applicationContext, EMPLOYEE_CREATION_VIEW_LOADING_FAILED, anchor_pane);
    }

    private void changeEmployeeStatus(EmployeeInfoDTO infoDTO) {

        employeeService.changeEmployeeIsActiveValue(infoDTO.getId());
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                clear_filters_button, this::cleanFiltersAndLiveSearch,
                new_employee_button, this::goToRegisterNewEmployeeView
        );

        configureRunnableMaps(map);
    }

    @Override
    public void configureLiveSearch() {

        search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        status_filter.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        hire_date_filter.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    @Override
    public void executeLiveSearch() {

        String employeeName = search_field.getText();

        EmployeeStatus selectedStatus = status_filter.getValue();

        if (selectedStatus == EmployeeStatus.TODOS) {

            selectedStatus = null;
        }

        HireDateRange selectedDateRange = hire_date_filter.getValue();

        List<EmployeeInfoDTO> employees = employeeService.liveSearch(employeeName, selectedStatus, selectedDateRange);

        cleanVBox(employee_list_container);

        loadEmployeeListOnView(employees);
    }

    @Override
    public void cleanFiltersAndLiveSearch() {

        setBlankTextfield(search_field);

        cleanComboBoxes(status_filter, hire_date_filter);
    }
}