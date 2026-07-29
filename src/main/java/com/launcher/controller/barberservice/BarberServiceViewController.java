package com.launcher.controller.barberservice;

import com.dto.barbershopservice.BarberServiceInfoDTO;
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

import static com.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.AnimationEngineConstants.ANIMATION_DELAY_IN_MS;
import static com.launcher.constants.MaterialDesignResources.MaterialIcon.DELETE_ICON;
import static com.launcher.constants.StringResource.ConfirmationDialog.*;
import static com.launcher.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.launcher.constants.StringResource.EmptyListMessage.EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED;
import static com.launcher.constants.ViewPath.BARBER_SERVICE_EDITION_VIEW_PATH;
import static com.launcher.constants.ViewPath.BARBER_SERVICE_ITEM_VIEW_PATH;
import static com.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.launcher.controller.helper.ContainerManager.*;
import static com.launcher.controller.helper.DialogHelper.showConfirmationDialog;
import static com.launcher.controller.helper.FXMLViewLoader.*;
import static com.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.*;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

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
            average_price,
            average_price_percentage_vs_last_month;

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
        loadServicesCreatedThisMonthVsLastMonth();
        loadServicesStatsByCategory();
        loadHighestRevenueStats();
        loadAveragePriceStats();
    }

    private void loadActiveServicesStats() {
        executeUITask(barberService::getServiceCount, uiActionValue -> setTextOnLabel(active_service_count, parseNumberValueToText(uiActionValue)));
        executeUITask(barberService::calculateServicesCreatedThisMonthVsLastMonth, uiActionValue -> setTextOnLabel(active_category_count, parseNumberValueToText(uiActionValue)));
    }

    private void loadServicesCreatedThisMonthVsLastMonth() {
        executeUITask(barberService::calculateServicesCreatedThisMonthVsLastMonth, uiActionValue -> setTextOnLabel(active_category_count, parseNumberValueToText(uiActionValue)));
    }

    private void loadServicesStatsByCategory() {
        executeUITask(
                saleService::getBarberServiceWithMostSales,
                barberServiceSalesStatsDTO -> {
                    setTextOnLabel(barber_service_with_most_sales, barberServiceSalesStatsDTO.getBarberServiceName());
                    setTextOnLabel(amount_of_sales, parseNumberValueToText(barberServiceSalesStatsDTO.getAmountOfSales()));
                }
        );
    }

    private void loadHighestRevenueStats() {
        executeUITask(
                saleService::getBarberServiceWithHighestRevenue,
                barberServiceRevenueStatsDTO -> {
                    setTextOnLabel(highest_revenue_service, barberServiceRevenueStatsDTO.getBarberServiceName());
                    setTextOnLabel(revenue_sum, parseNumberValueToText(barberServiceRevenueStatsDTO.getTotalRevenue()));
                }
        );
    }

    private void loadAveragePriceStats() {
        executeUITask(() -> List.of(barberService.getAveragePrice(), barberService.getAveragePricePercentageVsLastMonth()), uiActionValues -> {
            setTextOnLabel(average_price, CURRENCY_STRING_ARG + parseNumberValueToText(uiActionValues.getFirst()));
            setTextOnLabel(average_price_percentage_vs_last_month, formatAsPercentage(uiActionValues.getLast()) + "%");
        });
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
//TODO: rehacer toda la documentación de esta clase
// refactorizar estadísticas
// limpiar métodos sin uso
// modificar iconos de ser necesario
// eliminar tags de texto en el FXML