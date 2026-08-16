package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.presentation.controller.item.AbstractItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.presentation.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;

@Component
@Getter
@Setter
public class BarberServiceItemController extends AbstractItemController<BarberServiceInfoDTO> {

    private Consumer<BarberServiceInfoDTO>
            onEditCallback,
            onDeleteCallback;

    @FXML
    private Label
            serviceName,
            servicePrice,
            categoryText;

    @FXML
    private MFXButton
            editButton,
            deleteButton;

    @FXML
    public void initialize() {

        configureButtonActions();
    }

    private void goToEditBarberServiceView() {

        fire(onEditCallback);
    }

    private void goToDeleteBarberServiceView() {

        fire(onDeleteCallback);
    }

    public void setDataOnItem(BarberServiceInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        setTextOnLabel(serviceName, infoDTO.getName());
        setTextOnLabel(servicePrice, CURRENCY_STRING_ARG + infoDTO.getPrice().toString());
        setTextOnLabel(categoryText, infoDTO.getCategory().getDisplayName());
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditBarberServiceView,
                deleteButton, this::goToDeleteBarberServiceView
        );

        configureRunnableMaps(map);
    }
}