package com.barbershop.launcher.controller.implementation.client;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.launcher.controller.interfaces.CreationController;
import com.barbershop.service.interfaces.ClientService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.MaterialButton.MD_BUTTON_FILLED_OUTLINED;
import static com.barbershop.launcher.constants.ui.css_class.MaterialMFXInput.MATERIAL_MFX_INPUT;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.CLIENT_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.CLIENT_CREATION_VALIDATION_FAILED;
import static com.barbershop.launcher.constants.ui.messages.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.barbershop.launcher.constants.ui.prompt_text.ClientPromptText.*;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.HelperConstants.ACCEPT_BUTTON_TEXT;
import static com.barbershop.launcher.controller.helper.PopUpWindowHelper.showWindowAlert;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;
import static com.barbershop.launcher.controller.helper.ValidationFormatter.getConstraintViolationsList;
import static com.barbershop.launcher.controller.helper.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class ClientCreationController implements CreationController {

    private final ClientService clientService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    public VBox
            phones_container;

    @FXML
    private MFXButton
            back_button,
            add_phone_button,
            clean_all_fields_button,
            save_button;

    @FXML
    private TextField
            dni_field,
            first_name_field,
            last_name_field,
            email_field,
            optional_notes_field;

    @FXML
    public void initialize() {

        configurePromptTexts();

        configureButtonActions();

        addPhoneToList();
    }

    public void registerNewClient() {

        String dni = dni_field.getText();
        String firstName = first_name_field.getText();
        String lastName = last_name_field.getText();
        String email = email_field.getText();
        List<String> phoneList = extractPhoneNumbersFromPhoneContainer();
        String optionalNotes = optional_notes_field.getText();

        ClientCreationDTO creationDTO = buildDTOFromAttributes(dni, firstName, lastName, email, phoneList, optionalNotes);

        try {

            clientService.registerNewClient(creationDTO);

            showToastNotification(
                    anchor_pane,
                    applicationContext,
                    CLIENT_CREATION_TOAST_NOTIFICATION_MESSAGE,
                    ToastNotificationType.SUCCESSFUL
            );

        } catch (ConstraintViolationException exception) {

            String errorMessage = getConstraintViolationsList(exception);

            showWindowAlert(VALIDATION_ERROR_TITLE, CLIENT_CREATION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT);
        }
    }

    private void cleanPhoneNumbersList() {

        cleanContainer(phones_container);
        addPhoneToList();
    }

    private void addPhoneToList() {

        int phoneContainerSpaceValue = 10;
        Pos phoneContainerAligment = Pos.CENTER_LEFT;

        HBox hbox = createPhoneHBox(phoneContainerSpaceValue, phoneContainerAligment);

        MFXTextField phoneTextfield = createPhoneNumberTextfield();

        MFXButton removePhoneButton = createRemoveButton(hbox);

        addAllChildrensToPane(hbox, phoneTextfield, removePhoneButton);

        loadItemOnVBox(phones_container, hbox);
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
        removePhoneButton.setOnAction(_ -> phones_container.getChildren().remove(hbox));
        removePhoneButton.setText("Eliminar");

        return removePhoneButton;
    }

    private ClientCreationDTO buildDTOFromAttributes(
            String dni,
            String firstName,
            String lastName,
            String email,
            List<String> phoneList,
            String optionalNotes
    ) {
        return ClientCreationDTO.builder()
                .nationalIdentityCardNumber(dni)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumbersList(phoneList)
                .optionalNotes(optionalNotes)
                .build();
    }

    private List<String> extractPhoneNumbersFromPhoneContainer() {

        List<String> phones = new ArrayList<>();

        for (Node node : phones_container.getChildren()) {

            if (node instanceof HBox hbox) {

                TextField textFieldToExtract = (TextField) hbox.getChildren().getFirst();

                String extractedPhone = textFieldToExtract.getText();

                if (!extractedPhone.isBlank()) phones.add(extractedPhone);
            }
        }

        return phones;
    }

    @Override
    public void configurePromptTexts() {

        Map<TextField, String> map = Map.of(
                dni_field, CLIENT_NATIONAL_ID_CARD_NUMBER,
                first_name_field, CLIENT_NAME,
                last_name_field, CLIENT_SURNAME,
                email_field, CLIENT_EMAIL,
                optional_notes_field, CLIENT_OPTIONAL_NOTES
        );

        setPromptTextOnMap(map);
    }

    @Override
    public void resetForm() {

        cleanTextfields(List.of(dni_field, first_name_field, last_name_field, email_field, optional_notes_field));
        cleanPhoneNumbersList();
    }

    @Override
    public void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.CLIENTS, anchor_pane, applicationContext),
                clean_all_fields_button, this::resetForm,
                add_phone_button, this::addPhoneToList,
                save_button, this::registerNewClient
        );

        configureRunnableMaps(map);
    }
}
