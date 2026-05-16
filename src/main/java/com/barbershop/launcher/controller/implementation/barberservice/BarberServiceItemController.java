package com.barbershop.launcher.controller.implementation.barberservice;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.launcher.controller.interfaces.ItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.barbershop.launcher.constants.ui.messages.GenericStrings.CURRENCY_STRING_ARG;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.configureRunnableMaps;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.setTextOnLabel;

@Component
@Getter
@Setter
public class BarberServiceItemController implements ItemController<BarberServiceInfoDTO> {

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
    private MFXButton edit_button;

    @FXML
    private MFXButton delete_button;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void goToEditBarberServiceView() {

        if (onEditCallback != null) onEditCallback.accept(infoDTOReference);
    }

    private void goToDeleteBarberServiceView() {

        if (onDeleteCallback != null) onDeleteCallback.accept(infoDTOReference);
    }

    @Override
    public void setDataOnItem(BarberServiceInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        setTextOnLabel(service_name, infoDTO.getName());
        setTextOnLabel(service_price, CURRENCY_STRING_ARG + infoDTO.getPrice().toString());
        setTextOnLabel(category_text, infoDTO.getCategory().getDisplayName());
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditBarberServiceView,
                delete_button, this::goToDeleteBarberServiceView
        );

        configureRunnableMaps(map);
    }
}
