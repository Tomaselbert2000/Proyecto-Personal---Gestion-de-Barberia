package com.launcher.controller.implementation.employee;

import com.dto.employee.EmployeeInfoDTO;
import com.enums.EmployeeStatus;
import com.enums.HireDateRange;
import com.launcher.controller.helper.UIBasicComponents;
import com.launcher.controller.interfaces.ViewController;
import com.service.interfaces.EmployeeService;
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

import static com.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.launcher.animation.AnimationEngineConstant.ANIMATION_DELAY_IN_MS;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.ui.messages.EmptyListMessage.EMPTY_EMPLOYEE_LIST_MESSAGE;
import static com.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.launcher.constants.view.ViewPath.*;
import static com.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ContainerManager.*;
import static com.launcher.controller.helper.FXMLViewLoader.*;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.*;

@Component
@RequiredArgsConstructor
public class EmployeeViewController implements ViewController {

    private final ApplicationContext applicationContext;
    private final EmployeeService employeeService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label
            total_employees_count,
            total_employees_trend,
            active_employees_count,
            active_employees_percentage,
            inactive_employees_count,
            inactive_employees_percentage,
            average_productivity_count,
            productivity_trend,
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

        executeUITask(
                () -> {
                    Long count = employeeService.getEmployeeCount();
                    Long employeesTrendCount = employeeService.getEmployeesTrendThisMonth();

                    List<Label> labels = List.of(total_employees_count, total_employees_trend, results_count);
                    List<String> texts = List.of(parseNumberValueToText(count), parseNumberValueToText(employeesTrendCount), parseNumberValueToText(count));

                    return generateMap(labels, texts);
                },
                UIBasicComponents::setTextsOnLabelMap
        );
    }

    private void loadEmployeeActivityStats(int totalCount) {

        executeUITask(
                () -> {
                    Long activeEmployeesCount = employeeService.getActiveEmployees();
                    Long inactiveEmployeesCount = employeeService.getInactiveEmployees();

                    return List.of(activeEmployeesCount, inactiveEmployeesCount);
                },
                uiActionValues -> {

                    double activePercentage;
                    double inactivePercentage;

                    if (totalCount > 0) {

                        activePercentage = (uiActionValues.getFirst() * 100.0) / totalCount;
                        inactivePercentage = (uiActionValues.getLast() * 100.0) / totalCount;

                    } else {

                        activePercentage = 0.0;
                        inactivePercentage = 0.0;
                    }

                    Map<Label, String> map = Map.of(
                            active_employees_count, parseNumberValueToText(uiActionValues.getFirst()),
                            inactive_employees_count, parseNumberValueToText(uiActionValues.getLast()),
                            active_employees_percentage, formatAsPercentage(activePercentage),
                            inactive_employees_percentage, formatAsPercentage(inactivePercentage)
                    );

                    setTextsOnLabelMap(map);
                }
        );
    }

    private void loadProductivityStats() {

        executeUITask(
                () -> {
                    double averageMonthlyProductivity = employeeService.calculateAverageMonthlyProductivity();
                    double productivityTrend = employeeService.calculateProductivityTrendVsLastMonth();

                    return List.of(averageMonthlyProductivity, productivityTrend);
                },
                uiActionValues -> {

                    setTextOnLabel(average_productivity_count, formatAsDecimalValue(uiActionValues.getFirst()));

                    if (uiActionValues.getLast() > 0.0) {

                        setTextOnLabel(productivity_trend, "+" + formatAsPercentage(uiActionValues.getLast()));

                    } else {

                        setTextOnLabel(productivity_trend, "-" + formatAsPercentage(uiActionValues.getLast()));
                    }
                }
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

        cleanContainer(employee_list_container);

        loadEmployeeListOnView(employees);
    }

    @Override
    public void cleanFiltersAndLiveSearch() {

        setBlankTextfield(search_field);

        cleanComboBoxes(status_filter, hire_date_filter);
    }
}