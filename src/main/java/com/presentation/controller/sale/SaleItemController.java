package com.presentation.controller.sale;

import com.dto.sale.SaleInfoDTO;
import com.presentation.controller.AbstractItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.function.Consumer;

import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextsOnLabelMap;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.format.PersonNameFormatter.fullName;

@Component
@Getter
@Setter
public class SaleItemController extends AbstractItemController<SaleInfoDTO> {

    private Consumer<SaleInfoDTO>
            onSaleCancelCallBack,
            onViewDetailsCallBack;

    @FXML
    private Label
            clientFullName,
            saleTotal,
            barberServiceName,
            saleDate,
            employeeFullName,
            paymentMethodName,
            productsSoldCount;

    @FXML
    private MFXButton
            cancelSaleButton,
            viewSaleDetailButton;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(cancelSaleButton, this::cancelSale),
                Map.entry(viewSaleDetailButton, this::showSaleDetails)
        );

        configureRunnableMaps(map);
    }

    @Override
    public void setDataOnItem(SaleInfoDTO item) {

        this.infoDTOReference = item;

        LocalDate day = item.getDateAndTime().toLocalDate();
        int hour = item.getDateAndTime().getHour();
        int minute = item.getDateAndTime().getMinute();

        Map<Label, String> map = Map.ofEntries(
                Map.entry(clientFullName, fullName(item.getClientFirstName(), item.getClientLastName())),
                Map.entry(saleTotal, parseNumberValueToText(item.getTotal())),
                Map.entry(barberServiceName, item.getBarberServiceName() == null ? "-" : item.getBarberServiceName()),
                Map.entry(saleDate, String.format(DATETIME_SUMMARY_FORMAT, day, hour, minute)),
                Map.entry(employeeFullName, fullName(item.getEmployeeFirstName(), item.getEmployeeLastName())),
                Map.entry(paymentMethodName, item.getPaymentMethodName()),
                Map.entry(productsSoldCount, parseNumberValueToText(item.getReceiptItems().size()))
        );

        setTextsOnLabelMap(map);
    }

    public void showSaleDetails() {

        fire(onViewDetailsCallBack);
    }

    public void cancelSale() {

        fire(onSaleCancelCallBack);
    }
}
