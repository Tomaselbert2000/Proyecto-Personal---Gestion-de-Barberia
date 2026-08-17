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
    private AnchorPane anchorPane;

    @FXML
    private MFXButton
            backButton,
            addPhoneButton,
            cancelButton,
            saveButton;

    @FXML
    private TextField
            dniField,
            firstNameField,
            lastNameField,
            emailField,
            optionalNotesField;

    @FXML
    private VBox phonesContainer;

    @FXML
    private CheckBox client_active_checkbox;

    @FXML
    public void initialize(ClientInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        loadClientDataForEdition(infoDTO);
    }

    @Override
    protected AnchorPane getAnchorPane() {

        return anchorPane;
    }

    @Override
    protected void persistEntity(ClientUpdateDTO dto) {

        clientService.updateClient(infoDTOReference.getNationalIdentityCardNumber(), dto);
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
    protected void configureButtonActions() {

    }

    @Override
    protected void configurePromptTexts() {

    }

    @Override
    protected ClientUpdateDTO buildDTO() {

        return null;
    }

    @Override
    protected void resetForm() {

    }

    private void loadClientDataForEdition(ClientInfoDTO infoDTO) {
    }
}