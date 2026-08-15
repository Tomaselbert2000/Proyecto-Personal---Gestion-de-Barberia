package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.enums.BarberServiceCategory;
import com.enums.PriceRanges;
import com.enums.ToastNotificationType;
import com.presentation.controller.item.BaseCatalogViewController;
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

import static com.enums.ViewRedirection.BARBER_SERVICE_CREATION;
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
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class BarberServiceViewController extends BaseCatalogViewController<BarberServiceInfoDTO> {

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
            timesUsed,
            servicesCount;

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
        loadItemsOnView(catalog);

        loadEnumsOnComboBox(serviceCategorySelector, BarberServiceCategory.values());
        loadEnumsOnComboBox(servicePriceRangeSelector, PriceRanges.values());

        setStringConverter(serviceCategorySelector, BarberServiceCategory.TODOS);
        setStringConverter(servicePriceRangeSelector, PriceRanges.TODOS);

        attachLiveSearchListeners(
                serviceSearchField.textProperty(),
                serviceCategorySelector.valueProperty(),
                servicePriceRangeSelector.valueProperty()
        );

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

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                createBarberServiceButton, () -> redirectToView(BARBER_SERVICE_CREATION, getAnchorPane(), getApplicationContext()),
                cleanFiltersButton, this::resetSearchFilter
        );

        configureRunnableMaps(map);
    }

    @Override
    protected Label getResultsCountLabel() {

        return servicesCount;
    }

    @Override
    protected List<BarberServiceInfoDTO> searchCatalog() {

        Double minPrice = null, maxPrice = null;

        String serviceName = serviceSearchField.getText();

        BarberServiceCategory selectedCategory = serviceCategorySelector.getValue();

        if (selectedCategory == BarberServiceCategory.TODOS) selectedCategory = null;

        PriceRanges selectedPriceRange = servicePriceRangeSelector.getValue();

        if (selectedPriceRange != null) {
            minPrice = selectedPriceRange.getMinPrice();
            maxPrice = selectedPriceRange.getMaxPrice();
        }

        return barberService.liveSearch(serviceName, selectedCategory, minPrice, maxPrice);
    }

    @Override
    protected VBox getItemListContainer() {

        return servicesListViewBox;
    }

    @Override
    protected void loadItemsOnView(List<BarberServiceInfoDTO> items) {

        loadItemsOnController(
                items,
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

    @Override
    protected void clearFilterNodes() {

        setBlankTextfield(serviceSearchField);
        cleanComboBoxes(serviceCategorySelector, servicePriceRangeSelector);
    }
}