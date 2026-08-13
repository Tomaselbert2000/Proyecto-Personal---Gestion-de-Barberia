package com.presentation.controller.client;

import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.ClientService;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ClientEditionController extends BaseCrudFormController<ClientUpdateDTO, ClientInfoDTO> {

    private final ClientService clientService;

    public ClientEditionController(ApplicationContext applicationContext, ClientService clientService) {

        super(applicationContext);
        this.clientService = clientService;
    }

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
    protected AnchorPane getAnchorPane() {

        return anchor_pane;
    }

    @Override
    protected void persistEntity(ClientUpdateDTO dto) {

        clientService.updateClient(internalInfoDTOReference.getNationalIdentityCardNumber(), dto);
    }

    @Override
    protected String getSuccessMessage() {
        return "";
    }

    @Override
    protected String getErrorMessage() {
        return "";
    }

    @Override
    protected ClientUpdateDTO buildDTO() {
        return null;
    }

    @Override
    protected void resetForm() {

    }
}