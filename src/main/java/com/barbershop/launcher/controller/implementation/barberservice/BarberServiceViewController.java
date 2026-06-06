package com.barbershop.launcher.controller.implementation.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.PriceRanges;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.launcher.controller.interfaces.ViewController;
import com.barbershop.service.interfaces.BarberserviceService;
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

import static com.barbershop.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.barbershop.launcher.animation.AnimationEngineConstant.ANIMATION_DELAY_IN_MS;
import static com.barbershop.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.barbershop.launcher.constants.ui.messages.ConfirmationDialogMessage.*;
import static com.barbershop.launcher.constants.ui.messages.EmptyListMessage.EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.GenericStrings.*;
import static com.barbershop.launcher.constants.ui.messages.ViewLoadingErrorMessage.*;
import static com.barbershop.launcher.constants.view.ViewPath.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.*;
import static com.barbershop.launcher.controller.helper.ComboBoxHelper.loadEnumsOnComboBox;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.DialogHelper.showConfirmationDialog;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.*;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class BarberServiceViewController implements ViewController {

    private final BarberserviceService barberService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label
            service_count,
            new_services_this_month_count,
            category_count,
            highest_category_count,
            highest_price,
            lowest_price,
            average_price,
            average_price_percentage_vs_last_month,
            service_list_vbox_service_count;

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

        loadTotalServicesStats();

        loadServicesCreatedThisMonthVsLastMonth();

        loadServicesStatsByCategory();

        loadLowestAndHighestPricesStats();

        loadAveragePriceStats();
    }

    private void loadTotalServicesStats() {

        executeUITask(
                barberService::getServiceCount,
                uiActionValue -> {

                    setTextOnLabel(service_count, parseNumberValueToText(uiActionValue));
                    setTextOnLabel(service_list_vbox_service_count, parseNumberValueToText(uiActionValue) + " servicios encontrados");
                }
        );
    }

    private void loadServicesCreatedThisMonthVsLastMonth() {

        executeUITask(
                barberService::calculateServicesCreatedThisMonthVsLastMonth,
                uiActionValue -> setTextOnLabel(new_services_this_month_count, parseNumberValueToText(uiActionValue))
        );
    }

    private void loadServicesStatsByCategory() {

        executeUITask(
                barberService::getCategoryCount,
                uiActionValue -> setTextOnLabel(category_count, parseNumberValueToText(uiActionValue))
        );
    }

    private void loadLowestAndHighestPricesStats() {

        executeUITask(
                () -> {
                    Double highestPrice = barberService.getHighestPrice();
                    Double lowestPrice = barberService.getLowestPrice();

                    return List.of(highestPrice, lowestPrice);
                },
                uiActionValues -> {

                    setTextOnLabel(highest_price, CURRENCY_STRING_ARG + parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(lowest_price, CURRENCY_STRING_ARG + parseNumberValueToText(uiActionValues.getLast()));
                }
        );
    }

    private void loadAveragePriceStats() {

        executeUITask(
                () -> {
                    Double averagePriceOfAllBarberServices = barberService.getAveragePrice();
                    Double averagePricePercentageVsLastMonth = barberService.getAveragePricePercentageVsLastMonth();

                    return List.of(averagePriceOfAllBarberServices, averagePricePercentageVsLastMonth);
                },
                uiActionValues -> {

                    setTextOnLabel(average_price, CURRENCY_STRING_ARG + parseNumberValueToText(uiActionValues.getFirst()));
                    setTextOnLabel(average_price_percentage_vs_last_month, formatAsPercentage(uiActionValues.getLast()) + "%");
                }
        );
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

                executeLiveSearch();

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

            for (int i = 0; i<barberServiceInfoDTOS.size(); i++) {

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
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                create_barber_service_button, this::goToBarberServiceCreationView,
                clean_filters_button, this::cleanFiltersAndLiveSearch
        );

        configureRunnableMaps(map);
    }

    @Override
    public void configureLiveSearch() {

        service_search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        service_category_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        service_price_range_selector.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    @Override
    public void executeLiveSearch() {

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

        cleanContainer(services_list_vbox);

        loadBarberServiceCatalogOnView(barberServices);
    }

    @Override
    public void cleanFiltersAndLiveSearch() {

        setBlankTextfield(service_search_field);
        cleanComboBoxes(service_category_selector, service_price_range_selector);
    }
}
