package com.presentation.controller.sale;

import com.dto.sale.SaleInfoDTO;
import com.presentation.support.view.ViewRedirectionHelper;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

import static com.enums.ViewRedirection.SALES;
import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.presentation.constants.StringResource.EmptyListMessage.EMPTY_RECEIPT_LIST_MESSAGE;
import static com.presentation.constants.StringResource.FxmlViewLoadingErrorMessage.RECEIPT_ITEM_VIEW_LOADING_FAILED;
import static com.presentation.constants.ViewPath.RECEIPT_ITEM_VIEW_PATH;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextsOnLabelMap;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.format.PersonNameFormatter.fullName;
import static com.presentation.support.view.ContainerManager.loadItemsOnController;

@Component
@RequiredArgsConstructor
@Getter
public class SaleDetailViewController {

    private final ViewRedirectionHelper viewRedirectionHelper;
    private final ApplicationContext applicationContext;

    private SaleInfoDTO infoDTOReference;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TitledPane titledPane;

    @FXML
    private VBox receiptListContainer;

    @FXML
    private Label
            datetime,
            clientFullName,
            employeeFullName,
            barberServiceName,
            paymentMethodName,
            saleTotal;

    @FXML
    private MFXButton
            backButton,
            downloadPdfButton;

    public void loadSaleData(SaleInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        configureButtonActions();

        loadSaleSummary();
    }

    private void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(SALES, anchorPane, applicationContext)),
                Map.entry(downloadPdfButton, this::generatePDF)
        );

        configureRunnableMaps(map);
    }

    private void loadSaleSummary() {

        LocalDate saleDateTime = infoDTOReference.getDateAndTime().toLocalDate();

        int exactStartHour = infoDTOReference.getDateAndTime().getHour();
        int exactStartMinute = infoDTOReference.getDateAndTime().getMinute();

        Map<Label, String> map = getStringMap(saleDateTime, exactStartHour, exactStartMinute);

        setTextsOnLabelMap(map);

        loadItemsOnController(
                infoDTOReference.getReceiptItems(),
                receiptListContainer,
                ReceiptItemController.class,
                RECEIPT_ITEM_VIEW_PATH,
                EMPTY_RECEIPT_LIST_MESSAGE,
                RECEIPT_ITEM_VIEW_LOADING_FAILED,
                _ -> {
                }
        );
    }

    private @NonNull Map<Label, String> getStringMap(LocalDate saleDateTime, int exactStartHour, int exactStartMinute) {
        String saleDateTimeAsString = String.format(DATETIME_SUMMARY_FORMAT, saleDateTime, exactStartHour, exactStartMinute);

        return Map.ofEntries(
                Map.entry(datetime, saleDateTimeAsString),
                Map.entry(clientFullName, fullName(infoDTOReference.getClientFirstName(), infoDTOReference.getClientLastName())),
                Map.entry(employeeFullName, fullName(infoDTOReference.getEmployeeFirstName(), infoDTOReference.getEmployeeLastName())),
                Map.entry(barberServiceName, infoDTOReference.getBarberServiceName()),
                Map.entry(paymentMethodName, infoDTOReference.getPaymentMethodName()),
                Map.entry(saleTotal, parseNumberValueToText(infoDTOReference.getTotal()))
        );
    }

    private void generatePDF() {

        //TODO: pendiente de diseño e implementación
    }
}
