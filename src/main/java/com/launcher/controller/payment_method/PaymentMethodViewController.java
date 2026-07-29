package com.launcher.controller.payment_method;

import com.dto.payment.PaymentMethodInfoDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;
import com.service.interfaces.PaymentMethodService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.PAYMENT_METHOD_CREATION;
import static com.launcher.animation.AnimationEngine.fadeNodeIn;
import static com.launcher.concurrency.ConcurrencyManager.executeUITask;
import static com.launcher.constants.AnimationEngineConstants.ANIMATION_DELAY_IN_MS;
import static com.launcher.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.launcher.constants.StringResource.EmptyListMessage.EMPTY_PAYMENT_LIST_MESSAGE;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.PAYMENT_METHOD_EDITION_VIEW_LOADING_FAILED;
import static com.launcher.constants.StringResource.FxmlViewLoadingErrorMessage.PAYMENT_METHOD_VIEW_LOADING_FAILED;
import static com.launcher.constants.ViewPath.PAYMENT_METHOD_EDITION_VIEW_PATH;
import static com.launcher.constants.ViewPath.PAYMENT_METHOD_ITEM_VIEW_PATH;
import static com.launcher.controller.helper.ComboBoxHelper.cleanComboBoxes;
import static com.launcher.controller.helper.ContainerManager.*;
import static com.launcher.controller.helper.FXMLViewLoader.*;
import static com.launcher.controller.helper.UIBasicComponents.*;
import static com.launcher.controller.helper.ValidationFormatter.parseNumberValueToText;
import static com.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class PaymentMethodViewController {

    private final ApplicationContext applicationContext;
    private final PaymentMethodService paymentMethodService;
    private final SaleService saleService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private Label
            most_used_payment_name,
            most_used_payment_usage,
            highest_revenue_payment_name,
            revenue_sum,
            active_payment_methods_count,
            modifier_type_balance,
            results_count;

    @FXML
    private TextField search_field;

    @FXML
    private ComboBox<PaymentMethodModifierType> modifier_type_filter;

    @FXML
    private ComboBox<PaymentMethodStatus> status_filter;

    @FXML
    private MFXButton
            clear_filters_button,
            create_payment_method_button;

    @FXML
    private VBox payment_method_list_container;

    @FXML
    public void initialize() {

        loadPaymentMethodStats();
        configureButtonActions();
        configureLiveSearch();
    }

    private void configureLiveSearch() {

        search_field.textProperty().addListener((_, _, _) -> executeLiveSearch());
        status_filter.valueProperty().addListener((_, _, _) -> executeLiveSearch());
        modifier_type_filter.valueProperty().addListener((_, _, _) -> executeLiveSearch());
    }

    private void executeLiveSearch() {

        String paymentName = search_field.getText();
        PaymentMethodStatus status = status_filter.getValue();
        PaymentMethodModifierType modifierType = modifier_type_filter.getValue();

        List<PaymentMethodInfoDTO> payments = paymentMethodService.paymentMethodLiveSearch(paymentName, status, modifierType);

        cleanContainer(payment_method_list_container);

        loadPaymentMethodList(payments);

        setTextOnLabel(results_count, parseNumberValueToText(payments.size()) + " encontrados");
    }

    private void cleanFiltersAndLiveSearch() {

        setBlankTextfield(search_field);
        cleanComboBoxes(status_filter, modifier_type_filter);
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                create_payment_method_button, this::createPaymentMethod,
                clear_filters_button, this::cleanFiltersAndLiveSearch
        );

        configureRunnableMaps(map);
    }

    private void loadPaymentMethodStats() {

        loadMostUsedPaymentMethodsStats();
        loadHighestRevenuePaymentsStats();
        loadActivePaymentsStats();
        loadModifierValueSumStats();

        loadPaymentMethodList(paymentMethodService.getPaymentMethodsList());
    }

    private void loadMostUsedPaymentMethodsStats() {

        executeUITask(
                saleService::getMostUsedPaymentMethod,

                dto -> {
                    setTextOnLabel(most_used_payment_name, dto.getPaymentMethodName());
                    setTextOnLabel(most_used_payment_usage, parseNumberValueToText(dto.getAmountOfSalesWhereIsUsed()));
                }
        );
    }

    private void loadHighestRevenuePaymentsStats() {

        executeUITask(
                saleService::getHighestRevenuePaymentMethod,

                dto -> {
                    setTextOnLabel(highest_revenue_payment_name, dto.getPaymentMethod());
                    setTextOnLabel(revenue_sum, CURRENCY_STRING_ARG + parseNumberValueToText(dto.getRevenueAmount()));
                }
        );
    }

    private void loadActivePaymentsStats() {

        executeUITask(
                paymentMethodService::getPaymentMethodCountMarkedAsActive,
                activePaymentsAmount -> setTextOnLabel(active_payment_methods_count, parseNumberValueToText(activePaymentsAmount))
        );
    }

    private void loadModifierValueSumStats() {

        executeUITask(
                saleService::getModifierValueSumAcrossAllSales,
                modifierValueSum -> {

                    if (modifierValueSum >= 0.0) {

                        setTextOnLabel(modifier_type_balance, "+" + CURRENCY_STRING_ARG + parseNumberValueToText(modifierValueSum));
                    } else {

                        setTextOnLabel(modifier_type_balance, "-" + CURRENCY_STRING_ARG + parseNumberValueToText(modifierValueSum));
                    }
                }
        );
    }

    private void loadPaymentMethodList(List<PaymentMethodInfoDTO> payments) {

        if (payments.isEmpty()) {

            showEmptyListLabel(EMPTY_PAYMENT_LIST_MESSAGE, payment_method_list_container);
        } else {

            for (int i = 0; i < payments.size(); i++) {

                PaymentMethodInfoDTO infoDTO = payments.get(i);

                FXMLLoader loader = generateLoaderWithPath(PAYMENT_METHOD_ITEM_VIEW_PATH);

                Parent paymentView = returnParentFromLoader(loader, PAYMENT_METHOD_VIEW_LOADING_FAILED);

                PaymentMethodItemController paymentMethodItemController = loader.getController();

                paymentMethodItemController.setDataOnItem(infoDTO);
                paymentMethodItemController.setOnEditCallback(this::goToPaymentMethodEditView);
                paymentMethodItemController.setOnActiveToggleCallback(this::togglePaymentMethodStatus);

                loadItemOnVBox(payment_method_list_container, paymentView);

                fadeNodeIn(payment_method_list_container, i * ANIMATION_DELAY_IN_MS);
            }
        }
    }

    private void togglePaymentMethodStatus(PaymentMethodInfoDTO paymentMethodInfoDTO) {

        paymentMethodService.togglePaymentMethodStatus(paymentMethodInfoDTO.getName());
    }

    private void createPaymentMethod() {

        redirectToView(PAYMENT_METHOD_CREATION, anchor_pane, applicationContext);
    }

    private void goToPaymentMethodEditView(PaymentMethodInfoDTO paymentMethodInfoDTO) {

        FXMLLoader loader = generateLoaderWithPath(PAYMENT_METHOD_EDITION_VIEW_PATH);
        setControllerOnLoader(loader, applicationContext);
        Parent paymentMethodEditionView = returnParentFromLoader(loader, PAYMENT_METHOD_EDITION_VIEW_LOADING_FAILED);
        PaymentMethodEditionController paymentMethodEditionController = loader.getController();
        paymentMethodEditionController.initialize(paymentMethodInfoDTO);
        setViewOnPaneCenter(anchor_pane, paymentMethodEditionView);
    }
}