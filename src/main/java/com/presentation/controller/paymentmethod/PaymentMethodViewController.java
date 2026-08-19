package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;
import com.presentation.controller.BaseCatalogViewController;
import com.service.interfaces.PaymentMethodService;
import com.service.interfaces.SaleService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ObservableValue;
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

import static com.enums.ViewRedirection.PAYMENT_METHOD_CREATION;
import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.support.format.PriceFormatter.format;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_PAYMENT_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.PAYMENT_METHOD_EDITION_VIEW_LOADING_FAILED;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.PAYMENT_METHOD_ITEM_VIEW_LOADING_FAILED;
import static com.presentation.constants.ViewPath.PAYMENT_METHOD_EDITION_VIEW_PATH;
import static com.presentation.constants.ViewPath.PAYMENT_METHOD_ITEM_VIEW_PATH;
import static com.presentation.support.control.ComboBoxHelper.resetComboBoxFilter;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
@RequiredArgsConstructor
public class PaymentMethodViewController extends BaseCatalogViewController<PaymentMethodInfoDTO> {

    private final ApplicationContext applicationContext;
    private final PaymentMethodService paymentMethodService;
    private final SaleService saleService;
    private final ViewRedirectionHelper viewRedirectionHelper;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label
            mostUsedPaymentName,
            mostUsedPaymentUsage,
            highestRevenuePaymentName,
            revenueSum,
            activePaymentMethodsCount,
            modifierTypeBalance,
            resultsCount;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<PaymentMethodModifierType> modifierTypeFilter;

    @FXML
    private ComboBox<PaymentMethodStatus> statusFilter;

    @FXML
    private MFXButton
            clearFiltersButton,
            createPaymentMethodButton;

    @FXML
    private VBox paymentMethodListContainer;

    @Override
    protected ObservableValue<?>[] getSearchProperties() {

        return new ObservableValue<?>[]{

                searchField.textProperty(),
                statusFilter.valueProperty(),
                modifierTypeFilter.valueProperty()
        };
    }

    @Override
    protected Label getResultsCountLabel() {

        return resultsCount;
    }

    @Override
    protected List<PaymentMethodInfoDTO> searchCatalog() {

        String paymentName = searchField.getText();
        PaymentMethodStatus status = statusFilter.getValue();
        PaymentMethodModifierType modifierType = modifierTypeFilter.getValue();

        return paymentMethodService.paymentMethodLiveSearch(paymentName, status, modifierType);
    }

    @Override
    protected VBox getItemListContainer() {

        return paymentMethodListContainer;
    }

    @Override
    protected void loadItemsOnView(List<PaymentMethodInfoDTO> items) {

        loadItemsOnController(
                items,
                paymentMethodListContainer,
                PaymentMethodItemController.class,
                PAYMENT_METHOD_ITEM_VIEW_PATH,
                EMPTY_PAYMENT_LIST_MESSAGE,
                PAYMENT_METHOD_ITEM_VIEW_LOADING_FAILED,
                itemController -> {

                    itemController.setOnEditCallback(this::goToPaymentMethodEditView);
                    itemController.setOnActiveToggleCallback(this::togglePaymentMethodStatus);
                }
        );
    }

    @Override
    protected void clearFilterNodes() {

        setBlankTextfield(searchField);
        resetComboBoxFilter(statusFilter, modifierTypeFilter);
    }

    @Override
    protected void initializeListContent() {

        loadItemsOnView(paymentMethodService.getPaymentMethodsList());
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(createPaymentMethodButton, this::createPaymentMethod),
                Map.entry(clearFiltersButton, this::resetSearchFilter)
        );

        configureRunnableMaps(map);
    }

    @Override
    protected void loadGlobalStats() {

        loadMostUsedPaymentMethodsStats();
        loadHighestRevenuePaymentsStats();
        loadActivePaymentsStats();
        loadModifierValueSumStats();
    }

    private void loadMostUsedPaymentMethodsStats() {

        executeAsyncTask(
                saleService::getMostUsedPaymentMethod,

                dto -> {
                    setTextOnLabel(mostUsedPaymentName, dto.getPaymentMethodName());
                    setTextOnLabel(mostUsedPaymentUsage, parseNumberValueToText(dto.getAmountOfSalesWhereIsUsed()));
                }
        );
    }

    private void loadHighestRevenuePaymentsStats() {

        executeAsyncTask(
                saleService::getHighestRevenuePaymentMethod,

                dto -> {
                    setTextOnLabel(highestRevenuePaymentName, dto.getPaymentMethod());
                    setTextOnLabel(revenueSum, format(dto.getRevenueAmount()));
                }
        );
    }

    private void loadActivePaymentsStats() {

        executeAsyncTask(
                paymentMethodService::getPaymentMethodCountMarkedAsActive,
                activePaymentsAmount -> setTextOnLabel(activePaymentMethodsCount, parseNumberValueToText(activePaymentsAmount))
        );
    }

    private void loadModifierValueSumStats() {

        executeAsyncTask(
                saleService::getModifierValueSumAcrossAllSales,
                modifierValueSum -> {

                    if (modifierValueSum >= 0.0) {

                        setTextOnLabel(modifierTypeBalance, "+" + format(modifierValueSum));
                    } else {

                        setTextOnLabel(modifierTypeBalance, "-" + format(modifierValueSum));
                    }
                }
        );
    }

    private void togglePaymentMethodStatus(PaymentMethodInfoDTO paymentMethodInfoDTO) {

        executeAsyncTask(
                () -> {
                    paymentMethodService.togglePaymentMethodStatus(paymentMethodInfoDTO.getName());
                    return null;
                },
                _ -> {
                }
        );
    }

    private void createPaymentMethod() {

        viewRedirectionHelper.redirectToView(PAYMENT_METHOD_CREATION, anchorPane, applicationContext);
    }

    private void goToPaymentMethodEditView(PaymentMethodInfoDTO infoDTO) {

        loadViewWithControllerPane(
                PAYMENT_METHOD_EDITION_VIEW_PATH,
                applicationContext,
                PAYMENT_METHOD_EDITION_VIEW_LOADING_FAILED,
                anchorPane,
                PaymentMethodEditionController.class,
                editionController -> editionController.initialize(infoDTO)
        );
    }
}