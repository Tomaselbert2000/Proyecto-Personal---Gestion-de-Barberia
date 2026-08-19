package com.presentation.controller.client;

import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.presentation.controller.BaseCrudFormController;
import com.service.interfaces.ClientService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.enums.ViewRedirection.CLIENTS;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialButton.MD_BUTTON_FILLED_OUTLINED;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialMFXInputComponent.MATERIAL_MFX_INPUT;
import static com.presentation.constants.PromptTexts.ClientPromptText.*;
import static com.presentation.constants.StringResource.ToastNotificationMessage.CLIENT_EDITION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.CLIENT_EDITION_VALIDATION_FAILED;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.view.ContainerManager.*;
import com.presentation.support.view.ViewRedirectionHelper;

@Component
public class ClientEditionController extends BaseCrudFormController<ClientUpdateDTO, ClientInfoDTO> {

    private final ClientService clientService;

    private final ViewRedirectionHelper viewRedirectionHelper;

    public ClientEditionController(ApplicationContext applicationContext, ClientService clientService, ViewRedirectionHelper viewRedirectionHelper) {

        super(applicationContext);
        this.clientService = clientService;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    @FXML
    public VBox phonesContainer;

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
    public void initialize(ClientInfoDTO infoDTO) {

        infoDTOReference = infoDTO;

        configurePromptTexts();

        configureButtonActions();

        loadClientDataForEdition(infoDTO);
    }

    private void loadClientDataForEdition(ClientInfoDTO infoDTO) {

        Map<TextField, String> map = Map.of(
                dniField, infoDTO.getNationalIdentityCardNumber(),
                firstNameField, infoDTO.getFirstName(),
                lastNameField, infoDTO.getLastName(),
                emailField, infoDTO.getEmail(),
                optionalNotesField, infoDTO.getOptionalNotes()
        );

        setTextsOnTextfieldMap(map);

        cleanContainer(phonesContainer);

        List<String> phones = infoDTO.getPhoneNumbersList();

        if (phones == null || phones.isEmpty()) {

            addPhoneToList("");

        } else {

            for (String phone : phones) {

                addPhoneToList(phone);
            }
        }
    }

    private void addPhoneToList(String initialValue) {

        int phoneContainerSpaceValue = 10;
        Pos phoneContainerAligment = Pos.CENTER_LEFT;

        HBox hbox = createPhoneHBox(phoneContainerSpaceValue, phoneContainerAligment);

        MFXTextField phoneTextfield = createPhoneNumberTextfield();

        phoneTextfield.setText(initialValue);

        MFXButton removePhoneButton = createRemoveButton(hbox);

        addAllChildrensToPane(hbox, phoneTextfield, removePhoneButton);

        loadItemOnVBox(phonesContainer, hbox);
    }

    private MFXTextField createPhoneNumberTextfield() {

        MFXTextField mfxTextField = new MFXTextField();
        mfxTextField.setPromptText(CLIENT_PHONE_PROMPT_TEXT);
        mfxTextField.getStyleClass().add(MATERIAL_MFX_INPUT);
        mfxTextField.maxWidth(Double.POSITIVE_INFINITY);

        return mfxTextField;
    }

    private MFXButton createRemoveButton(HBox hbox) {

        MFXButton removePhoneButton = new MFXButton();
        removePhoneButton.getStyleClass().add(MD_BUTTON_FILLED_OUTLINED);
        removePhoneButton.setOnAction(_ -> phonesContainer.getChildren().remove(hbox));
        removePhoneButton.setText("Eliminar");

        return removePhoneButton;
    }

    private List<String> extractPhoneNumbersFromPhoneContainer() {

        List<String> phones = new ArrayList<>();

        for (Node node : phonesContainer.getChildren()) {

            if (node instanceof HBox hbox) {

                TextField textFieldToExtract = (TextField) hbox.getChildren().getFirst();
                String extractedPhone = textFieldToExtract.getText();

                if (!extractedPhone.isBlank()) phones.add(extractedPhone);
            }
        }

        return phones;
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

        return CLIENT_EDITION_TOAST_NOTIFICATION_MESSAGE;
    }

    @Override
    protected String getErrorMessage() {

        return CLIENT_EDITION_VALIDATION_FAILED;
    }

    @Override
    protected ClientUpdateDTO buildDTO() {

        String dni = dniField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        List<String> phoneList = extractPhoneNumbersFromPhoneContainer();
        String optionalNotes = optionalNotesField.getText();

        return ClientUpdateDTO.builder()
                .nationalIdentityCardNumber(dni)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumbersList(phoneList)
                .optionalNotes(optionalNotes)
                .build();
    }

    @Override
    protected void resetForm() {

        loadClientDataForEdition(infoDTOReference);
    }

    @Override
    protected void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                dniField, CLIENT_NATIONAL_ID_CARD_NUMBER,
                firstNameField, CLIENT_NAME,
                lastNameField, CLIENT_SURNAME,
                emailField, CLIENT_EMAIL,
                optionalNotesField, CLIENT_OPTIONAL_NOTES
        );

        setPromptTextOnMap(map);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.ofEntries(
                Map.entry(backButton, () -> viewRedirectionHelper.redirectToView(CLIENTS, getAnchorPane(), getApplicationContext())),
                Map.entry(cancelButton, this::resetForm),
                Map.entry(addPhoneButton, () -> addPhoneToList("")),
                Map.entry(saveButton, this::saveEntity)
        );

        configureRunnableMaps(map);
    }
}
