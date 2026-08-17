package com.presentation.controller.client;

import com.dto.client.ClientInfoDTO;
import com.presentation.controller.AbstractItemController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.presentation.support.control.UIBasicComponents.configureRunnableMaps;
import static com.presentation.support.control.UIBasicComponents.setTextsOnLabelMap;

@Component
@Getter
@Setter
public class ClientItemController extends AbstractItemController<ClientInfoDTO> {

    private Consumer<ClientInfoDTO>
            onEditCallback,
            onSendMessageCallback;

    @FXML
    private Label
            clientFirstName,
            clientLastName,
            nationalIDCardNumber,
            mainPhoneNumber;

    @FXML
    private MFXButton
            editButton,
            sendWhatsAppMessageButton;

    @Override
    public void setDataOnItem(ClientInfoDTO item) {

        Map<Label, String> map = Map.of(
                clientFirstName, item.getFirstName(),
                clientLastName, item.getLastName(),
                nationalIDCardNumber, item.getNationalIdentityCardNumber(),
                mainPhoneNumber, getMainPhoneNumber(item)
        );

        setTextsOnLabelMap(map);
    }

    @Override
    protected void configureButtonActions() {

        Map<Button, Runnable> map = Map.of(
                editButton, this::goToEditClient,
                sendWhatsAppMessageButton, this::sendWhatsAppMessage
        );

        configureRunnableMaps(map);
    }

    public void goToEditClient() {

        fire(onEditCallback);
    }

    public void sendWhatsAppMessage() {

        fire(onSendMessageCallback);
    }

    private static String getMainPhoneNumber(ClientInfoDTO item) {

        List<String> phones = item.getPhoneNumbersList();

        return (phones != null && !phones.isEmpty()) ? phones.getFirst() : "";
    }
}
