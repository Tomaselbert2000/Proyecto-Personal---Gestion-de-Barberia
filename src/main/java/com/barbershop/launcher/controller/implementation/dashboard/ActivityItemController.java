package com.barbershop.launcher.controller.implementation.dashboard;

import com.barbershop.dto.dashboard.RecentActivityDTO;
import com.barbershop.enums.EventType;
import com.barbershop.launcher.controller.interfaces.ItemController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.barbershop.launcher.constants.ui.css_class.CssStylesStrings.*;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.generateMap;
import static com.barbershop.launcher.controller.helper.UIBasicComponents.setTextsOnLabelMap;

@Component
@Getter
@Setter
public class ActivityItemController implements ItemController<RecentActivityDTO> {

    private static LocalDateTime NOW = LocalDateTime.now();

    @FXML
    private Region activity_icon;

    @FXML
    private Label event_type;

    @FXML
    private Label text_to_attach;

    @FXML
    private Label timestamp;

    private String generateTimestampFromString(LocalDateTime timestamp) {

        long timeDifferenceInMinutes = ChronoUnit.MINUTES.between(timestamp, NOW);

        if (timeDifferenceInMinutes >= 60) {

            long hours = timeDifferenceInMinutes / 60;

            if (hours <= 1) return "Hace " + hours + " hora";

            return "Hace " + hours + " horas";

        } else {

            if (timeDifferenceInMinutes <= 1) return "Hace " + timeDifferenceInMinutes + " minuto";

            return "Hace " + timeDifferenceInMinutes + " minutos";
        }
    }

    private String selectIconBasedOnEventType(EventType eventType) {

        switch (eventType) {

            case TURNO_CANCELADO -> {

                return CANCELED_APPOINTMENT_ICON;
            }
            case TURNO_FINALIZADO -> {

                return COMPLETED_APPOINTMENT_ICON;
            }
            case EMPLEADO_INACTIVO -> {

                return INACTIVE_EMPLOYEE_ICON;
            }
            case EMPLEADO_DESVINCULADO -> {

                return EMPLOYEE_OUT_ICON;
            }
            case ALERTA_STOCK_BAJO -> {

                return LOW_STOCK_ALERT_ICON;
            }
            default -> {

                return DEFAULT_EVENT_ICON;
            }
        }
    }

    @Override
    public void setDataOnItem(RecentActivityDTO infoDTO) {

        String timestampAsString = generateTimestampFromString(infoDTO.getTimestamp());

        List<Label> labels = List.of(event_type, text_to_attach, timestamp);
        List<String> texts = List.of(infoDTO.getEventType().getDisplayName(), infoDTO.getText(), timestampAsString);
        Map<Label, String> map = generateMap(labels, texts);

        setTextsOnLabelMap(map);

        String styleClass = selectIconBasedOnEventType(infoDTO.getEventType());

        activity_icon.getStyleClass().add(styleClass);
    }

    @Override
    public void configureButtonActions() {
    }
}
