package com.presentation.controller.dashboard;

import com.dto.activity.RecentActivityDTO;
import com.enums.EventType;
import com.presentation.controller.item.AbstractItemController;
import com.utils.time.TimeCalculation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.presentation.constants.MaterialDesignResources.MaterialIcon.*;
import static com.presentation.support.control.UIBasicComponents.generateMap;
import static com.presentation.support.control.UIBasicComponents.setTextsOnLabelMap;

@Component
public class ActivityItemController extends AbstractItemController<RecentActivityDTO> {

    @FXML
    private Region activityIcon;

    @FXML
    private Label
            eventType,
            textToAttach,
            timestamp;

    private String generateTimestampFromString(LocalDateTime timestamp) {

        long timeDifferenceInMinutes = ChronoUnit.MINUTES.between(timestamp, TimeCalculation.getCurrentDateTime());

        if (timeDifferenceInMinutes >= 60) {

            long hours = timeDifferenceInMinutes / 60;
            return hours <= 1 ? "Hace " + hours + " hora" : "Hace " + hours + " horas";

        } else {

            return timeDifferenceInMinutes <= 1 ? "Hace " + timeDifferenceInMinutes + " minuto" : "Hace " + timeDifferenceInMinutes + " minutos";
        }
    }

    private String selectIconBasedOnEventType(EventType eventType) {

        return switch (eventType) {

            case NUEVO_TURNO, NUEVO_CLIENTE, NUEVO_EMPLEADO, NUEVO_PRODUCTO -> ADD_ALERT_ICON;
            case TURNO_CANCELADO -> CANCEL_PERSON_ICON;
            case TURNO_FINALIZADO -> DONE_ALL_ICON;
            case EMPLEADO_INACTIVO -> ENTERPRISE_OFF_ICON;
            case EMPLEADO_DESVINCULADO -> PERSON_REMOVE;
            case ALERTA_STOCK_BAJO -> WARNING_ICON;
        };
    }

    public void setDataOnItem(RecentActivityDTO infoDTO) {

        String timestampAsString = generateTimestampFromString(infoDTO.getTimestamp());
        List<Label> labels = List.of(eventType, textToAttach, timestamp);
        List<String> texts = List.of(infoDTO.getEventType().getDisplayName(), infoDTO.getText(), timestampAsString);
        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);
        activityIcon.getStyleClass().add(selectIconBasedOnEventType(infoDTO.getEventType()));
    }

    @Override
    protected void configureButtonActions() {
    }
}