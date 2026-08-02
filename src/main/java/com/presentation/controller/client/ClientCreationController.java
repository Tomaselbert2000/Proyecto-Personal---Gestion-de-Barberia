package com.presentation.controller.client;

import com.dto.client.ClientCreationDTO;
import com.enums.ToastNotificationType;
import com.enums.ViewRedirection;
import com.service.interfaces.ClientService;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.validation.ConstraintViolationException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialButton.MD_BUTTON_FILLED_OUTLINED;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialMFXInputComponent.MATERIAL_MFX_INPUT;
import static com.presentation.constants.PromptTexts.ClientPromptText.*;
import static com.presentation.constants.StringResource.DisplayString.ACCEPT_BUTTON_TEXT;
import static com.presentation.constants.StringResource.ToastNotificationMessage.CLIENT_CREATION_TOAST_NOTIFICATION_MESSAGE;
import static com.presentation.constants.StringResource.ValidationErrorMessage.CLIENT_CREATION_VALIDATION_FAILED;
import static com.presentation.constants.StringResource.ValidationErrorMessage.VALIDATION_ERROR_TITLE;
import static com.presentation.support.view.ContainerManager.*;
import static com.presentation.support.dialog.PopUpWindowHelper.showWindowAlert;
import static com.presentation.support.notification.ToastNotificationHelper.showToastNotification;
import static com.presentation.support.control.UIBasicComponents.*;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;
import static com.presentation.support.view.ViewRedirectionHelper.redirectToView;

@Component
@RequiredArgsConstructor
public class ClientCreationController {

    private final ClientService clientService;
    private final ApplicationContext applicationContext;

    @FXML
    public VBox phones_container;

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private MFXButton back_button, add_phone_button, clean_all_fields_button, save_button;

    @FXML
    private TextField dni_field, first_name_field, last_name_field, email_field, optional_notes_field;

    /**
     * Inicializa los componentes del formulario al cargar la vista.
     * Configura textos de prompt en campos, asigna acciones a botones y
     * prepara el contenedor para la gestión de números de teléfono.
     */
    public void initialize() {

        configurePromptTexts();

        configureButtonActions();

        addPhoneToList();
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * <p>
     * Extrae los datos del formulario, construye el DTO de creación y
     * delega la persistencia al servicio de clientes. En caso de éxito,
     * muestra una notificación toast. Si ocurren violaciones de validación,
     * presenta una alerta con los detalles de error.
     *
     * @throws ConstraintViolationException cuando los datos no cumplen las reglas de negocio
     */
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

            showWindowAlert(VALIDATION_ERROR_TITLE, CLIENT_CREATION_VALIDATION_FAILED, errorMessage, Alert.AlertType.ERROR, ACCEPT_BUTTON_TEXT, getCurrentWindow(anchor_pane));
        }
    }

    /**
     * Limpia la lista de números de teléfono almacenados en el contenedor.
     * Elimina todos los HBox existentes y re-inicializa con un nuevo campo vacío.
     */
    private void cleanPhoneNumbersList() {

        cleanContainer(phones_container);
        addPhoneToList();
    }

    /**
     * Agrega un nuevo campo de entrada para número de teléfono al contenedor.
     * Crea un HBox con un MFXTextField y un botón de eliminación,
     * configurando alineación y espaciado apropiados.
     */
    private void addPhoneToList() {

        int phoneContainerSpaceValue = 10;
        Pos phoneContainerAligment = Pos.CENTER_LEFT;

        HBox hbox = createPhoneHBox(phoneContainerSpaceValue, phoneContainerAligment);

        MFXTextField phoneTextfield = createPhoneNumberTextfield();

        MFXButton removePhoneButton = createRemoveButton(hbox);

        addAllChildrensToPane(hbox, phoneTextfield, removePhoneButton);

        loadItemOnVBox(phones_container, hbox);
    }

    /**
     * Crea un campo de texto MFX para la entrada de números de teléfono.
     * Configura el texto de prompt y estilos de Material Design.
     *
     * @return instancia de MFXTextField configurada
     */
    private MFXTextField createPhoneNumberTextfield() {

        MFXTextField mfxTextField = new MFXTextField();
        mfxTextField.setPromptText(CLIENT_PHONE_PROMPT_TEXT);
        mfxTextField.getStyleClass().add(MATERIAL_MFX_INPUT);
        mfxTextField.maxWidth(Double.POSITIVE_INFINITY);

        return mfxTextField;
    }

    /**
     * Crea un botón de eliminación para remover campos de teléfono del contenedor.
     * Al hacer clic, elimina el HBox correspondiente del padre.
     *
     * @param hbox contenedor al que se asignará el botón
     * @return instancia de MFXButton configurada con acción de eliminación
     */
    private MFXButton createRemoveButton(HBox hbox) {

        MFXButton removePhoneButton = new MFXButton();
        removePhoneButton.getStyleClass().add(MD_BUTTON_FILLED_OUTLINED);
        removePhoneButton.setOnAction(_ -> phones_container.getChildren().remove(hbox));
        removePhoneButton.setText("Eliminar");

        return removePhoneButton;
    }

    /**
     * Construye el DTO de creación de cliente a partir de los atributos del formulario.
     * Mapea los valores de los campos de texto y la lista de teléfonos al objeto DTO.
     *
     * @param dni           número de identificación nacional
     * @param firstName     nombre del cliente
     * @param lastName      apellido del cliente
     * @param email         dirección de correo electrónico
     * @param phoneList     lista de números de teléfono
     * @param optionalNotes notas opcionales del cliente
     * @return instancia de ClientCreationDTO con todos los datos mapeados
     */
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

    /**
     * Extrae los números de teléfono válidos del contenedor de teléfonos.
     * Itera sobre los HBox existentes y captura el texto de los campos de entrada,
     * excluyendo valores en blanco o nulos.
     *
     * @return lista de strings con los números de teléfono extraídos
     */
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

    /**
     * Configura los textos de prompt en todos los campos del formulario.
     * Utiliza un mapa que asocia cada campo con su texto de prompt correspondiente.
     */
    private void configurePromptTexts() {
        Map<TextField, String> map = Map.of(
                dni_field, CLIENT_NATIONAL_ID_CARD_NUMBER,
                first_name_field, CLIENT_NAME,
                last_name_field, CLIENT_SURNAME,
                email_field, CLIENT_EMAIL,
                optional_notes_field, CLIENT_OPTIONAL_NOTES
        );
        setPromptTextOnMap(map);
    }

    /**
     * Restablece el formulario a su estado inicial.
     * Limpia todos los campos de texto y elimina la lista de teléfonos almacenados.
     */
    private void resetForm() {
        cleanTextfields(List.of(dni_field, first_name_field, last_name_field, email_field, optional_notes_field));
        cleanPhoneNumbersList();
    }

    /**
     * Configura las acciones de todos los botones del formulario.
     * Asigna callbacks para navegación, limpieza, adición de teléfonos y persistencia.
     */
    private void configureButtonActions() {
        Map<Button, Runnable> map = Map.of(
                back_button, () -> redirectToView(ViewRedirection.CLIENTS, anchor_pane, applicationContext),
                clean_all_fields_button, this::resetForm,
                add_phone_button, this::addPhoneToList,
                save_button, this::registerNewClient
        );
        configureRunnableMaps(map);
    }
}