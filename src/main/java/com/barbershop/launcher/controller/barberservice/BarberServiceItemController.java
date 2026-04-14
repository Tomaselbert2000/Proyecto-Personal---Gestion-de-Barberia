package com.barbershop.launcher.controller.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.barbershop.launcher.constants.ui.messages.GenericStrings.CURRENCY_STRING_ARG;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.setTextOnLabel;

@Component
@Getter
@Setter
public class BarberServiceItemController {

    private BarberServiceInfoDTO infoDTOReference;

    private Consumer<BarberServiceInfoDTO> onEditCallback;
    private Consumer<BarberServiceInfoDTO> onDeleteCallback;

    @FXML
    private Label service_name;

    @FXML
    private Label service_price;

    @FXML
    private Label category_text;

    @FXML
    private Button edit_button;

    @FXML
    private Button delete_button;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void configureButtonActions() {

        edit_button.setOnAction(_ -> goToEditBarberServiceView());
        delete_button.setOnAction(_ -> goToDeleteBarberServiceView());
    }

    private void goToEditBarberServiceView() {

        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    private void goToDeleteBarberServiceView() {

        if (onDeleteCallback != null) onDeleteCallback.accept(infoDTOReference);
    }

    public void setDataOnItem(BarberServiceInfoDTO barberServiceInfoDTO) {

        infoDTOReference = barberServiceInfoDTO;

        setTextOnLabel(service_name, barberServiceInfoDTO.getName());
        setTextOnLabel(service_price, CURRENCY_STRING_ARG + barberServiceInfoDTO.getPrice().toString());
        setTextOnLabel(category_text, barberServiceInfoDTO.getCategory().getDisplayName());
    }
}
