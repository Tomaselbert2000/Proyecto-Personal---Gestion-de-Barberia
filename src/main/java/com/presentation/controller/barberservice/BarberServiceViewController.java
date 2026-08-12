package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.enums.BarberServiceCategory;
import com.enums.PriceRanges;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.service.interfaces.BarberserviceService;
import com.service.interfaces.SaleService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.DELETE_ICON;
import static com.presentation.constants.StringResource.ConfirmationDialog.*;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED;
import static com.presentation.constants.ViewPath.BARBER_SERVICE_EDITION_VIEW_PATH;
import static com.presentation.constants.ViewPath.BARBER_SERVICE_ITEM_VIEW_PATH;
import static com.presentation.support.control.ComboBoxHelper.cleanComboBoxes;
import static com.presentation.support.control.ComboBoxHelper.loadEnumsOnComboBox;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
import static com.presentation.support.dialog.DialogHelper.showConfirmationDialog;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.view.ContainerManager.*;
import static com.presentation.support.view.FXMLViewLoader.*;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class BarberServiceViewController {

    private final BarberserviceService barberService;
    private final SaleService saleService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label
            activeServiceCount,
            activeCategoryCount,
            barberServiceWithMostSales,
            amountOfSales,
            highestRevenueService,
            revenueSum,
            lowestUsedService,
            timesUsed;

    @FXML
    private TextField serviceSearchField;

    @FXML
    private ComboBox<BarberServiceCategory> serviceCategorySelector;

    @FXML
    private ComboBox<PriceRanges> servicePriceRangeSelector;

    @FXML
    private MFXButton
            cleanFiltersButton,
            createBarberServiceButton;

    @FXML
    private VBox servicesListViewBox;

    @FXML
    public void initialize() {
        List<BarberServiceInfoDTO> catalog = barberService.getServicesList();
        loadServicesStats();
        loadBarberServiceCatalogOnView(catalog);
        loadEnumsOnComboBox(serviceCategorySelector, BarberServiceCategory.values());
        loadEnumsOnComboBox(servicePriceRangeSelector, PriceRanges.values());
        setStringConverter(serviceCategorySelector, BarberServiceCategory.TODOS);
        setStringConverter(servicePriceRangeSelector, PriceRanges.TODOS);
        configureLiveSearch();
        configureButtonActions();
    }

    private void loadServicesStats() {
        loadActiveServicesStats();
        loadMostValuableBarberServiceStats();
        loadHighestRevenueStats();
        loadLeastUsedStats();
    }

    private void loadActiveServicesStats() {

        executeAsyncTask(
                barberService::getActiveOnCatalogStats,
                barberServiceActiveCatalogStatsDTO -> {
                    setTextOnLabel(activeServiceCount, parseNumberValueToText(barberServiceActiveCatalogStatsDTO.getAmountOfActiveServices()));
                    setTextOnLabel(activeCategoryCount, "En " + parseNumberValueToText(barberServiceActiveCatalogStatsDTO.getAmountOfDifferentCategories()) + " categorias distintas");
                }
        );
    }

    private void loadMostValuableBarberServiceStats() {
        executeAsyncTask(
                saleService::getBarberServiceWithMostSales,
                barberServiceSalesStatsDTO -> {
                    setTextOnLabel(barberServiceWithMostSales, barberServiceSalesStatsDTO.getBarberServiceName());
                    setTextOnLabel(amountOfSales, parseNumberValueToText(barberServiceSalesStatsDTO.getAmountOfSales()) + " ventas realizadas");
                }
        );
    }

    private void loadHighestRevenueStats() {
        executeAsyncTask(
                saleService::getBarberServiceWithHighestRevenue,
                barberServiceRevenueStatsDTO -> {
                    setTextOnLabel(highestRevenueService, barberServiceRevenueStatsDTO.getBarberServiceName());
                    setTextOnLabel(revenueSum, "Total recaudado " + CURRENCY_STRING_ARG + parseNumberValueToText(barberServiceRevenueStatsDTO.getTotalRevenue()));
                }
        );
    }

    private void loadLeastUsedStats() {
        executeAsyncTask(
                saleService::getBarberServiceWithLowestUsage,
                barberServiceLeastUsedStatsDTO -> {
                    setTextOnLabel(lowestUsedService, barberServiceLeastUsedStatsDTO.getBarberServiceName());
                    setTextOnLabel(timesUsed, "Solo " + parseNumberValueToText(barberServiceLeastUsedStatsDTO.getTotalUsage()) + " realizados");
                }
        );
    }

    private void goToBarberServiceEditionView(BarberServiceInfoDTO infoDTO) {

        loadViewWithControllerPane(
                BARBER_SERVICE_EDITION_VIEW_PATH,
                applicationContext,
                BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED,
                anchorPane,
                BarberServiceEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }

    private void confirmAndDeleteService(BarberServiceInfoDTO barberServiceInfoDTO) {
        Runnable onConfirm = () -> {
            try {
                barberService.deleteBarberservice(barberServiceInfoDTO.getBarberServiceId());
                showToastNotification(anchorPane, applicationContext, BARBER_SERVICE_SUCCESSFULLY_DELETED_MESSAGE, ToastNotificationType.SUCCESSFUL);
                executeLiveSearch();
            } catch (DataIntegrityViolationException exception) {
                showToastNotification(anchorPane, applicationContext, BARBER_SERVICE_DELETION_FAILED_MESSAGE, ToastNotificationType.FAILED);
            }
        };
        Runnable onCancel = () -> {
        };
        showConfirmationDialog(
                anchorPane,
                applicationContext,
                BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_TITLE,
                BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_MESSAGE,
                CANCEL_BUTTON_TEXT,
                CONFIRM_BUTTON_TEXT,
                DELETE_ICON,
                onConfirm,
                onCancel);
    }

    private void loadBarberServiceCatalogOnView(List<BarberServiceInfoDTO> barberServiceInfoDTOS) {

        loadItemsOnController(
                barberServiceInfoDTOS,
                servicesListViewBox,
                BarberServiceItemController.class,
                BARBER_SERVICE_ITEM_VIEW_PATH,
                EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE,
                BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnEditCallback(this::goToBarberServiceEditionView);
                    itemController.setOnDeleteCallback(this::confirmAndDeleteService);
                }
        );
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                createBarberServiceButton, () -> redirectToView(ViewRedirection.BARBER_SERVICE_CREATION, anchorPane, applicationContext),
                cleanFiltersButton, this::cleanFiltersAndLiveSearch
        );
        configureRunnableMaps(map);
    }

    private void configureLiveSearch() {
        serviceSearchField.textProperty().addListener((_, _, _) -> executeLiveSearch());
        serviceCategorySelector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        servicePriceRangeSelector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    private void executeLiveSearch() {
        Double minPrice = null, maxPrice = null;
        String serviceName = serviceSearchField.getText();
        BarberServiceCategory selectedCategory = serviceCategorySelector.getValue();
        if (selectedCategory == BarberServiceCategory.TODOS)
            selectedCategory = null;
        PriceRanges selectedPriceRange = servicePriceRangeSelector.getValue();
        if (selectedPriceRange != null) {
            minPrice = selectedPriceRange.getMinPrice();
            maxPrice = selectedPriceRange.getMaxPrice();
        }
        List<BarberServiceInfoDTO> barberServices = barberService.liveSearch(serviceName, selectedCategory, minPrice, maxPrice);
        cleanContainer(servicesListViewBox);
        loadBarberServiceCatalogOnView(barberServices);
    }

    private void cleanFiltersAndLiveSearch() {
        setBlankTextfield(serviceSearchField);
        cleanComboBoxes(serviceCategorySelector, servicePriceRangeSelector);
    }
}