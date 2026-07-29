package com.launcher.controller.barberservice;

import com.dto.barbershopservice.BarberServiceInfoDTO;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.launcher.constants.StringResource.DisplayString.CURRENCY_STRING_ARG;
import static com.launcher.controller.helper.UIBasicComponents.configureRunnableMaps;
import static com.launcher.controller.helper.UIBasicComponents.setTextOnLabel;

@Component
@Getter
@Setter
public class BarberServiceItemController {

    private BarberServiceInfoDTO infoDTOReference;

    private Consumer<BarberServiceInfoDTO>
            onEditCallback,
            onDeleteCallback;

    @FXML
    private Label
            service_name,
            service_price,
            category_text;

    @FXML
    private MFXButton
            edit_button,
            delete_button;

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
    public void setDataOnItem(BarberServiceInfoDTO infoDTO) {
        infoDTOReference = infoDTO;
        setTextOnLabel(service_name, infoDTO.getName());
        setTextOnLabel(service_price, CURRENCY_STRING_ARG + infoDTO.getPrice().toString());
        setTextOnLabel(category_text, infoDTO.getCategory().getDisplayName());
    }

    /**
     * Configura las acciones de los botones en la interfaz de usuario.
     */
    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                edit_button, this::goToEditBarberServiceView,
                delete_button, this::goToDeleteBarberServiceView
        );
        configureRunnableMaps(map);
    }
}