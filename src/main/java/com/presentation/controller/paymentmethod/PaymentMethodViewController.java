package com.presentation.controller.paymentmethod;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;
import com.presentation.controller.item.BaseCatalogViewController;
import com.service.interfaces.PaymentMethodService;
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

import static com.enums.ViewRedirection.PAYMENT_METHOD_CREATION;
import static com.presentation.concurrency.ConcurrencyManager.executeAsyncTask;
import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_PAYMENT_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.PAYMENT_METHOD_EDITION_VIEW_LOADING_FAILED;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.PAYMENT_METHOD_ITEM_VIEW_LOADING_FAILED;
import static com.presentation.constants.ViewPath.PAYMENT_METHOD_EDITION_VIEW_PATH;
import static com.presentation.constants.ViewPath.PAYMENT_METHOD_ITEM_VIEW_PATH;
import static com.presentation.support.control.ComboBoxHelper.cleanComboBoxes;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;
import static com.presentation.support.view.FXMLViewLoader.loadViewWithControllerPane;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class PaymentMethodViewController extends BaseCatalogViewController<PaymentMethodInfoDTO> {

    private final ApplicationContext applicationContext;
    private final PaymentMethodService paymentMethodService;
    private final SaleService saleService;

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

    @FXML
    public void initialize() {

        loadPaymentMethodStats();

        configureButtonActions();

        configureLiveSearch();
    }

    private void configureLiveSearch() {

        attachLiveSearchListeners(
                searchField.textProperty(),
                statusFilter.valueProperty(),
                modifierTypeFilter.valueProperty()
        );
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
        cleanComboBoxes(statusFilter, modifierTypeFilter);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                createPaymentMethodButton, this::createPaymentMethod,
                clearFiltersButton, this::resetSearchFilter
        );

        configureRunnableMaps(map);
    }

    private void loadPaymentMethodStats() {

        loadMostUsedPaymentMethodsStats();
        loadHighestRevenuePaymentsStats();
        loadActivePaymentsStats();
        loadModifierValueSumStats();

        loadItemsOnView(paymentMethodService.getPaymentMethodsList());
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
                    setTextOnLabel(revenueSum, CURRENCY_STRING_ARG + parseNumberValueToText(dto.getRevenueAmount()));
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

                        setTextOnLabel(modifierTypeBalance, "+" + CURRENCY_STRING_ARG + parseNumberValueToText(modifierValueSum));
                    } else {

                        setTextOnLabel(modifierTypeBalance, "-" + CURRENCY_STRING_ARG + parseNumberValueToText(modifierValueSum));
                    }
                }
        );
    }

    private void togglePaymentMethodStatus(PaymentMethodInfoDTO paymentMethodInfoDTO) {

        paymentMethodService.togglePaymentMethodStatus(paymentMethodInfoDTO.getName());
    }

    private void createPaymentMethod() {

        redirectToView(PAYMENT_METHOD_CREATION, anchorPane, applicationContext);
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