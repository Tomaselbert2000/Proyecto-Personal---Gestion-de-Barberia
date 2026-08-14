package com.presentation.support.control;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import static com.presentation.constants.StringResource.DisplayString.*;

public final class StatusBadgeHelper {

    public static void applyBadge(
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
}
