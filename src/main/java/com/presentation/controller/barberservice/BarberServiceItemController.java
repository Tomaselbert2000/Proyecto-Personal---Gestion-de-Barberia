package com.presentation.controller.barberservice;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.presentation.controller.item.ItemController;
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
public class BarberServiceItemController implements ItemController<BarberServiceInfoDTO> {

    private BarberServiceInfoDTO infoDTOReference;

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

    /**
     * Inicializa el controlador, configurando las acciones de los botones.
     */
    @FXML
    public void initialize() {
        configureButtonActions();
    }

    /**
     * Navega a la vista de edición del servicio de barbero.
     */
    private void goToEditBarberServiceView() {
        if (onEditCallback != null)
            onEditCallback.accept(infoDTOReference);
    }

    /**
     * Navega a la vista de eliminación del servicio de barbero.
     */
    private void goToDeleteBarberServiceView() {
        if (onDeleteCallback != null)
            onDeleteCallback.accept(infoDTOReference);
    }

    /**
     * Establece los datos del servicio de barbero en el elemento de interfaz de usuario.
     *
     * @param infoDTO Datos del servicio de barbero a mostrar.
     */

    @Override
    public void setDataOnItem(BarberServiceInfoDTO infoDTO) {
        infoDTOReference = infoDTO;
        setTextOnLabel(serviceName, infoDTO.getName());
        setTextOnLabel(servicePrice, CURRENCY_STRING_ARG + infoDTO.getPrice().toString());
        setTextOnLabel(categoryText, infoDTO.getCategory().getDisplayName());
    }

    /**
     * Configura las acciones de los botones en la interfaz de usuario.
     */
    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditBarberServiceView,
                deleteButton, this::goToDeleteBarberServiceView
        );
        configureRunnableMaps(map);
    }
}