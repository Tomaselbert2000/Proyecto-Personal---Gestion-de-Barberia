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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

import static com.presentation.animation.AnimationEngine.fadeNodeIn;
import static com.presentation.concurrency.ConcurrencyManager.executeUITask;
import static com.presentation.animation.AnimationEngineConstants.ANIMATION_DELAY_IN_MS;
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
import static com.presentation.support.view.ContainerManager.*;
import static com.presentation.support.dialog.DialogHelper.showConfirmationDialog;
import static com.presentation.support.view.FXMLViewLoader.*;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.control.ValidationFormatter.setStringConverter;
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
    private AnchorPane anchor_pane;

    @FXML
    private Label
            active_service_count,
            active_category_count,
            barber_service_with_most_sales,
            amount_of_sales,
            highest_revenue_service,
            revenue_sum,
            lowest_used_service,
            times_used;

    @FXML
    private TextField service_search_field;

    @FXML
    private ComboBox<BarberServiceCategory> service_category_selector;

    @FXML
    private ComboBox<PriceRanges> service_price_range_selector;

    @FXML
    private MFXButton
            clean_filters_button,
            create_barber_service_button;

    @FXML
    private VBox services_list_vbox;

    @FXML
    public void initialize() {
        List<BarberServiceInfoDTO> catalog = barberService.getServicesList();
        loadServicesStats();
        loadBarberServiceCatalogOnView(catalog);
        loadEnumsOnComboBox(service_category_selector, BarberServiceCategory.values());
        loadEnumsOnComboBox(service_price_range_selector, PriceRanges.values());
        setStringConverter(service_category_selector, BarberServiceCategory.TODOS);
        setStringConverter(service_price_range_selector, PriceRanges.TODOS);
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

        executeUITask(
                barberService::getActiveOnCatalogStats,
                barberServiceActiveCatalogStatsDTO -> {
                    setTextOnLabel(active_service_count, parseNumberValueToText(barberServiceActiveCatalogStatsDTO.getAmountOfActiveServices()));
                    setTextOnLabel(active_category_count, "En " + parseNumberValueToText(barberServiceActiveCatalogStatsDTO.getAmountOfDifferentCategories()) + " categorias distintas");
                }
        );
    }

    private void loadMostValuableBarberServiceStats() {
        executeUITask(
                saleService::getBarberServiceWithMostSales,
                barberServiceSalesStatsDTO -> {
                    setTextOnLabel(barber_service_with_most_sales, barberServiceSalesStatsDTO.getBarberServiceName());
                    setTextOnLabel(amount_of_sales, parseNumberValueToText(barberServiceSalesStatsDTO.getAmountOfSales()) + " ventas realizadas");
                }
        );
    }

    private void loadHighestRevenueStats() {
        executeUITask(
                saleService::getBarberServiceWithHighestRevenue,
                barberServiceRevenueStatsDTO -> {
                    setTextOnLabel(highest_revenue_service, barberServiceRevenueStatsDTO.getBarberServiceName());
                    setTextOnLabel(revenue_sum, "Total recaudado " + CURRENCY_STRING_ARG + parseNumberValueToText(barberServiceRevenueStatsDTO.getTotalRevenue()));
                }
        );
    }

    private void loadLeastUsedStats() {
        executeUITask(
                saleService::getBarberServiceWithLowestUsage,
                barberServiceLeastUsedStatsDTO -> {
                    setTextOnLabel(lowest_used_service, barberServiceLeastUsedStatsDTO.getBarberServiceName());
                    setTextOnLabel(times_used, "Solo " + parseNumberValueToText(barberServiceLeastUsedStatsDTO.getTotalUsage()) + " realizados");
                }
        );
    }

    private void goToBarberServiceEditionView(BarberServiceInfoDTO infoDTO) {
        FXMLLoader loader = generateLoaderWithPath(BARBER_SERVICE_EDITION_VIEW_PATH);
        setControllerOnLoader(loader, applicationContext);
        Parent barberServiceEditionView = returnParentFromLoader(loader, BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED);
        BarberServiceEditionController barberServiceEditionController = loader.getController();
        barberServiceEditionController.initialize(infoDTO);
        setViewOnPaneCenter(anchor_pane, barberServiceEditionView);
    }

    private void confirmAndDeleteService(BarberServiceInfoDTO barberServiceInfoDTO) {
        Runnable onConfirm = () -> {
            try {
                barberService.deleteBarberservice(barberServiceInfoDTO.getBarberServiceId());
                showToastNotification(anchor_pane, applicationContext, BARBER_SERVICE_SUCCESSFULLY_DELETED_MESSAGE, ToastNotificationType.SUCCESSFUL);
                executeLiveSearch();
            } catch (DataIntegrityViolationException exception) {
                showToastNotification(anchor_pane, applicationContext, BARBER_SERVICE_DELETION_FAILED_MESSAGE, ToastNotificationType.FAILED);
            }
        };
        Runnable onCancel = () -> {
        };
        showConfirmationDialog(anchor_pane, applicationContext, BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_TITLE, BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_MESSAGE, CANCEL_BUTTON_TEXT, CONFIRM_BUTTON_TEXT, DELETE_ICON, onConfirm, onCancel);
    }

    private void loadBarberServiceCatalogOnView(List<BarberServiceInfoDTO> barberServiceInfoDTOS) {
        if (barberServiceInfoDTOS.isEmpty())
            showEmptyListLabel(EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE, services_list_vbox);
        else for (int i = 0; i < barberServiceInfoDTOS.size(); i++) {
            BarberServiceInfoDTO infoDTO = barberServiceInfoDTOS.get(i);
            FXMLLoader loader = generateLoaderWithPath(BARBER_SERVICE_ITEM_VIEW_PATH);
            Parent catalogItem = returnParentFromLoader(loader, BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED);
            BarberServiceItemController barberServiceItemController = loader.getController();
            barberServiceItemController.setDataOnItem(infoDTO);
            barberServiceItemController.setOnEditCallback(this::goToBarberServiceEditionView);
            barberServiceItemController.setOnDeleteCallback(this::confirmAndDeleteService);
            loadItemOnVBox(services_list_vbox, catalogItem);
            fadeNodeIn(services_list_vbox, i * ANIMATION_DELAY_IN_MS);
        }
    }

    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                create_barber_service_button, () -> redirectToView(ViewRedirection.BARBER_SERVICE_CREATION, anchor_pane, applicationContext),
                clean_filters_button, this::cleanFiltersAndLiveSearch
        );
        configureRunnableMaps(map);
    }

    private void configureLiveSearch() {
        service_search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        service_category_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        service_price_range_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    private void executeLiveSearch() {
        Double minPrice = null, maxPrice = null;
        String serviceName = service_search_field.getText();
        BarberServiceCategory selectedCategory = service_category_selector.getValue();
        if (selectedCategory == BarberServiceCategory.TODOS)
            selectedCategory = null;
        PriceRanges selectedPriceRange = service_price_range_selector.getValue();
        if (selectedPriceRange != null) {
            minPrice = selectedPriceRange.getMinPrice();
            maxPrice = selectedPriceRange.getMaxPrice();
        }
        List<BarberServiceInfoDTO> barberServices = barberService.liveSearch(serviceName, selectedCategory, minPrice, maxPrice);
        cleanContainer(services_list_vbox);
        loadBarberServiceCatalogOnView(barberServices);
    }

    private void cleanFiltersAndLiveSearch() {
        setBlankTextfield(service_search_field);
        cleanComboBoxes(service_category_selector, service_price_range_selector);
    }
}