package com.launcher.controller.implementation.client;

import com.dto.client.ClientInfoDTO;
import com.launcher.controller.interfaces.EditionController;
import com.service.interfaces.ClientService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientEditionController implements EditionController<ClientInfoDTO> {

    private final ApplicationContext applicationContext;
    private final ClientService clientService;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton
            back_button,
            add_phone_button,
            cancel_button,
            save_button;

    @FXML
    private TextField
            dni_field,
            first_name_field,
            last_name_field,
            email_field,
            optional_notes_field;

    @FXML
    private VBox phones_container;

    @FXML
    private CheckBox client_active_checkbox;

    @FXML
    public void initialize() {


    }

    @Override
    public void configureButtonActions(ClientInfoDTO infoDTO) {

    }

    @Override
    public void resetForm(ClientInfoDTO infoDTO) {

    }

    @Override
    public void configureButtonActions() {

    }
}