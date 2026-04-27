package com.barbershop.launcher.controller.client;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.enums.ToastNotificationType;
import com.barbershop.enums.ViewRedirection;
import com.barbershop.service.interfaces.ClientService;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.*;
import static com.barbershop.launcher.constants.ui.messages.ToastNotificationMessage.CLIENT_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.barbershop.launcher.constants.ui.prompt_text.ClientPromptText.*;
import static com.barbershop.launcher.controller.helper.ContainerManager.*;
import static com.barbershop.launcher.controller.helper.FXMLViewLoader.redirectToView;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showExceptionErrorMessage;
import static com.barbershop.launcher.controller.helper.ToastNotificationHelper.showToastNotification;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.*;

@Component
@RequiredArgsConstructor
public class ClientCreationController {

    private final ClientService clientService;
    private final ApplicationContext applicationContext;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    public VBox phones_container;

    @FXML
    public VBox error_message_container;

    @FXML
    public Label error_message_label;

    @FXML
    private Button back_button;

    @FXML
    private TextField dni_field;

    @FXML
    private TextField first_name_field;

    @FXML
    private TextField last_name_field;

    @FXML
    private TextField email_field;

    @FXML
    private Button add_phone_button;

    @FXML
    private TextField optional_notes_field;

    @FXML
    private Button clean_all_fields_button;

    @FXML
    private Button save_button;

    private List<TextField> textfields;

    @FXML
    public void initialize() {

        textfields = List.of(dni_field, first_name_field, last_name_field, email_field, optional_notes_field);
        List<String> promptText = List.of(CLIENT_NATIONAL_ID_CARD_NUMBER, CLIENT_NAME, CLIENT_SURNAME, CLIENT_EMAIL, CLIENT_OPTIONAL_NOTES);
        Map<TextField, String> map = generateMap(textfields, promptText);

        setPromptTextOnMap(map);

        configureButtonActions();

        addPhoneToList();
    }

    @FXML
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

        } catch (RuntimeException exception) {

            showExceptionErrorMessage(exception, error_message_label, error_message_container);
        }
    }

    private void configureButtonActions() {

        back_button.setOnAction((_) -> redirectToView(applicationContext, ViewRedirection.CLIENTS));

        clean_all_fields_button.setOnAction(_ -> resetForm());

        add_phone_button.setOnAction(_ -> addPhoneToList());

        save_button.setOnAction(_ -> registerNewClient());
    }

    private void resetForm() {

        cleanTextfields(textfields);
        cleanPhoneNumbersList();
    }

    private void cleanPhoneNumbersList() {

        cleanVBox(phones_container);
        addPhoneToList();
    }

    private void addPhoneToList() {

        HBox hbox = createHBox();

        TextField textField = createPhoneNumberTextfield();

        Button removePhoneButton = createRemoveButton(hbox);

        addNodesToHBox(hbox, textField, removePhoneButton);

        loadItemOnVBox(phones_container, hbox);
    }

    private TextField createPhoneNumberTextfield() {

        TextField textField = new TextField();
        textField.setPromptText(CLIENT_PHONE_PROMPT_TEXT);
        textField.getStyleClass().add(PHONE_NUMBER_CONTAINER_TEXTFIELD_STYLE_CLASS);
        HBox.setHgrow(textField, Priority.ALWAYS);

        return textField;
    }

    private Button createRemoveButton(HBox hbox) {

        Button removePhoneButton = new Button();
        removePhoneButton.getStyleClass().add(PHONE_NUMBER_CONTAINER_REMOVE_PHONE_BUTTON_STYLE_CLASS);
        removePhoneButton.setOnAction(_ -> phones_container.getChildren().remove(hbox));

        Region regionComponentForIcon = new Region();

        regionComponentForIcon.getStyleClass().add(PHONE_NUMBER_REGION_ICON);

        removePhoneButton.setGraphic(regionComponentForIcon);

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
}
