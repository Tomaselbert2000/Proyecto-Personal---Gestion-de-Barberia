package com.barbershop.launcher.controller.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.PriceRanges;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.service.interfaces.BarberserviceService;
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

import static com.barbershop.launcher.constants.ui.messages.UIMessages.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.UI_RenderingFunctions.*;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class BarberServiceController {

    private final BarberserviceService barberService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label service_count;

    @FXML
    private Label new_services_this_month_count;

    @FXML
    private Label category_count;

    @FXML
    private Label highest_category_count;

    @FXML
    private Label highest_price;

    @FXML
    private Label lowest_price;

    @FXML
    private Label average_price;

    @FXML
    private Label average_price_percentage_vs_last_month;

    @FXML
    private TextField service_search_field;

    @FXML
    private ComboBox<BarberServiceCategory> service_category_selector;

    @FXML
    private ComboBox<PriceRanges> service_price_range_selector;

    @FXML
    private Button clean_filters_button;

    @FXML
    private Button create_barber_service_button;

    @FXML
    private VBox services_list_vbox;

    @FXML
    private Label service_list_vbox_service_count;

    @FXML
    public void initialize() {

        List<BarberServiceInfoDTO> catalog = barberService.getServicesList();

        loadServicesStats();

        loadBarberServiceCatalogOnView(catalog);

        loadEnumsOnComboBox(service_category_selector, BarberServiceCategory.values());
        loadEnumsOnComboBox(service_price_range_selector, PriceRanges.values());

        setStringConverter(service_category_selector, BarberServiceCategory.TODOS);
        setStringConverter(service_price_range_selector, PriceRanges.TODOS);

        configureBarberServiceLiveSearch();
        configureButtonActions();
    }

    private void configureButtonActions() {

        create_barber_service_button.setOnAction(_ -> goToBarberServiceCreationView());
        clean_filters_button.setOnAction(_ -> cleanFiltersAndSearchfield());
    }

    private void configureBarberServiceLiveSearch() {

        service_search_field.textProperty().addListener((_, _, _) -> executeBarberServiceLiveSearch());
        service_category_selector.valueProperty().addListener((_, _, _) -> executeBarberServiceLiveSearch());
        service_price_range_selector.valueProperty().addListener((_, _, _) -> executeBarberServiceLiveSearch());
    }

    private void executeBarberServiceLiveSearch() {

        Double minPrice = null;
        Double maxPrice = null;

        String serviceName = service_search_field.getText();

        BarberServiceCategory selectedCategory = service_category_selector.getValue();

        if (selectedCategory == BarberServiceCategory.TODOS) {

            selectedCategory = null;
        }

        PriceRanges selectedPriceRange = service_price_range_selector.getValue();

        if (selectedPriceRange != null) {

            minPrice = selectedPriceRange.getMinPrice();
            maxPrice = selectedPriceRange.getMaxPrice();
        }

        List<BarberServiceInfoDTO> barberServices = barberService.liveSearch(serviceName, selectedCategory, minPrice, maxPrice);

        cleanVBox(services_list_vbox);

        loadBarberServiceCatalogOnView(barberServices);
    }

    private void cleanFiltersAndSearchfield() {

        setBlankTextfield(service_search_field);
        cleanComboBoxes(service_category_selector, service_price_range_selector);
    }

    private void loadServicesStats() {

        loadTotalServicesStats();

        loadServicesCreatedThisMonthVsLastMonth();

        loadServicesStatsByCategory();

        loadLowestAndHighestPricesStats();

        loadAveragePriceStats();
    }

    private void loadTotalServicesStats() {

        Long servicesCount = barberService.getServiceCount();

        setTextOnLabel(service_count, servicesCount.toString());

        setTextOnLabel(service_list_vbox_service_count, servicesCount + " servicios encontrados");
    }

    private void loadServicesCreatedThisMonthVsLastMonth() {

        Long servicesCreatedThisMonth = barberService.calculateServicesCreatedThisMonthVsLastMonth();

        setTextOnLabel(new_services_this_month_count, servicesCreatedThisMonth.toString());
    }

    private void loadServicesStatsByCategory() {

        Long categoryCount = barberService.getCategoryCount();

        setTextOnLabel(category_count, categoryCount.toString());
    }

    private void loadLowestAndHighestPricesStats() {

        Double highestPrice = barberService.getHighestPrice();

        setTextOnLabel(highest_price, CURRENCY_STRING_ARG + highestPrice.toString());

        Double lowestPrice = barberService.getLowestPrice();

        setTextOnLabel(lowest_price, CURRENCY_STRING_ARG + lowestPrice.toString());
    }

    private void loadAveragePriceStats() {

        Double averagePriceOfAllBarberServices = barberService.getAveragePrice();

        setTextOnLabel(average_price, CURRENCY_STRING_ARG + averagePriceOfAllBarberServices.toString());

        Double averagePricePercentageVsLastMonth = barberService.getAveragePricePercentageVsLastMonth();

        setTextOnLabel(average_price_percentage_vs_last_month, averagePricePercentageVsLastMonth.toString() + "%");
    }

    private void goToBarberServiceCreationView() {

        loadViewOnPane(BARBER_SERVICE_CREATION_VIEW_PATH, applicationContext, BARBER_SERVICE_CREATION_VIEW_LOADING_FAILED, anchor_pane);
    }

    private void goToBarberServiceEditionView(BarberServiceInfoDTO infoDTO) {

        FXMLLoader loader = generateLoaderWithPath(BARBER_SERVICE_EDITION_VIEW_PATH);

        setControllerOnLoader(loader, applicationContext);

        Parent barberServiceEditionView = returnParentFromLoader(loader, BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED);

        BarberServiceEditionController barberServiceEditionController = loader.getController();

        barberServiceEditionController.initialize(infoDTO);

        setViewOnAnchorPaneCenter(anchor_pane, barberServiceEditionView);
    }

    private void confirmAndDeleteService(BarberServiceInfoDTO barberServiceInfoDTO) {

        Runnable onConfirm = () -> {

            try {

                barberService.deleteBarberservice(barberServiceInfoDTO.getBarberServiceId());

                showToastNotification(
                        anchor_pane,
                        applicationContext,
                        BARBER_SERVICE_SUCCESSFULLY_DELETED_MESSAGE,
                        ToastNotificationType.SUCCESSFUL
                );

                executeBarberServiceLiveSearch();

            } catch (DataIntegrityViolationException exception) {

                showToastNotification(
                        anchor_pane,
                        applicationContext,
                        BARBER_SERVICE_DELETION_FAILED_MESSAGE,
                        ToastNotificationType.FAILED
                );
            }
        };

        Runnable onCancel = () -> {
        };

        showConfirmationDialog(
                anchor_pane,
                applicationContext,
                BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_TITLE,
                BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_MESSAGE,
                onConfirm,
                onCancel
        );
    }

    private void loadBarberServiceCatalogOnView(List<BarberServiceInfoDTO> barberServiceInfoDTOS) {

        if (barberServiceInfoDTOS.isEmpty()) {

            showEmptyListLabel(EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE, services_list_vbox);

        } else {

            for (BarberServiceInfoDTO infoDTO : barberServiceInfoDTOS) {

                FXMLLoader loader = generateLoaderWithPath(BARBER_SERVICE_ITEM_VIEW_PATH);

                Parent catalogItem = returnParentFromLoader(loader, BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED);

                BarberServiceItemController barberServiceItemController = loader.getController();

                barberServiceItemController.setDataOnItem(infoDTO);

                barberServiceItemController.setOnEditCallback(this::goToBarberServiceEditionView);
                barberServiceItemController.setOnDeleteCallback(this::confirmAndDeleteService);

                loadItemOnVBox(services_list_vbox, catalogItem);
            }
        }
    }
}
