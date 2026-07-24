package com.launcher.constants;

import javafx.geometry.Pos;

import java.time.format.DateTimeFormatter;

/**
 * Clase que contiene constantes utilizadas en diferentes controladores de la aplicación.
 * Define valores estándar y formatos que se utilizan en los controladores.
 */

public final class ControllerConstants {

    private ControllerConstants() {
    }

    public static final class AppointmentControllerConstants {

        public static final String DATETIME_SUMMARY_FORMAT = "%2s a las %02d:%02d";
        public static final int APPOINTMENT_DEFAULT_DURATION_IN_MINUTES = 30;
        public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    }

    public static final class EmployeeControllerConstants {

        public static final String ACTIVATE = "Activar";
        public static final String DEACTIVATE = "Desactivar";
        public static final Double PERCENTAGE_VALUE_IF_TEXTFIELD_IS_NULL = -1.0;
    }

    public static final class LoginControllerConstants {

        public static final String RETRY_LOGIN_BUTTON_TEXT = "Reintentar";
    }

    public static final class ProductControllerConstants {

        public static final String PLACEHOLDER_PATH = "/images/placeholder-image.jpg";
    }

    public static final class ToastNotificationControllerConstants {

        public static final Integer TOAST_PROGRESS_BAR_WIDTH_VALUE_ON_START = 400;
        public static final Double TOAST_PROGRESS_BAR_WIDTH_VALUE_ON_FINISH = 0.0;
        public static final Integer TOAST_NOTIFICATION_DURATION_IN_SECONDS = 5;
    }

    public static final class SettingsControllerConstants {

        public static final boolean DEFAULT_CHECKBOX_STATE = true;
        public static final int THEME_CONTAINER_SPACING = 16;
        public static final int THEME_CARD_SPACING = 8;
        public static final Pos DEFAULT_THEME_CARD_POS = Pos.CENTER_LEFT;
        public static final int THEME_TRANSITION_DURATION_IN_MS = 400;
        public static final int MIN_PASSWORD_LENGTH = 10;
        public static final int MAX_PASSWORD_LENGTH = 32;
        public static final String PASSWORD_GENERATED_SUCCESFULLY = "Contraseña generada exitosamente";
        public static final String CREDENTIALS_UPDATE_SUCCESSFULLY = "Credenciales actualizadas exitosamente";
    }
}
