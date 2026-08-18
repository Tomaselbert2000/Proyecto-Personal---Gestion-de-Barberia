package com.presentation.support.control;

import com.enums.AppointmentStatus;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import static com.enums.AppointmentStatus.*;
import static com.enums.AppointmentStatus.CANCELADO;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialDesignStyleClass.*;
import static com.presentation.constants.MaterialDesignResources.MaterialIcon.MaterialDesignStyles.MaterialDesignStyleClass.CANCELED_BADGE;
import static com.presentation.constants.StringResource.DisplayString.*;
import static com.presentation.support.control.UIBasicComponents.addLabelStyle;
import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;

public final class StatusBadgeHelper {

    public static void applyIsActiveBadge(
            Boolean isActive,
            Label statusLabel,
            Region badge,
            Button toggleButton,
            String activeClass,
            String inactiveClass) {

        if (isActive) {

            statusLabel.setText(ACTIVE_STATUS_LABEL);
            badge.setStyle(activeClass);
            toggleButton.setText(DEACTIVATE_BUTTON_TEXT);

        } else {

            statusLabel.setText(INACTIVE_STATUS_LABEL);
            badge.setStyle(inactiveClass);
            toggleButton.setText(ACTIVATE_BUTTON_TEXT);
        }
    }

    public static void applyAppointmentStatusBadge(AppointmentStatus status, Label statusBadge) {

        statusBadge.getStyleClass().clear();
        addLabelStyle(statusBadge, BADGE);

        switch (status) {

            case PROGRAMADO, REPROGRAMADO:

                addLabelStyle(statusBadge, SCHEDULED_BADGE);
                setTextOnLabel(statusBadge, status == PROGRAMADO ? PROGRAMADO.getDisplayName() : REPROGRAMADO.getDisplayName());
                break;

            case FINALIZADO:

                addLabelStyle(statusBadge, COMPLETED_BADGE);
                setTextOnLabel(statusBadge, FINALIZADO.getDisplayName());
                break;

            case CANCELADO:

                addLabelStyle(statusBadge, CANCELED_BADGE);
                setTextOnLabel(statusBadge, CANCELADO.getDisplayName());
                break;
        }
    }
}
