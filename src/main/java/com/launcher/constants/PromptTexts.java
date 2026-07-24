package com.launcher.constants;

public final class PromptTexts {

    private PromptTexts() {
    }

    public static final class AppointmentPromptText {

        public static final String APPOINTMENT_CLIENT_NAME = "Ingrese nombre o apellido del cliente";
        public static final String APPOINTMENT_NOTES = "Agregar notas o comentarios sobre el turno";
    }

    public static final class AppUserPromptText {

        public static final String USERNAME = "Nombre de nuevo usuario";
        public static final String PASSWORD = "Contraseña";
        public static final String CONFIRM_PASSWORD = "Confirmar contraseña";
    }

    public static final class BarberServicePromptText {

        public static final String BARBER_SERVICE_NAME = "Ej: Corte Clásico";
        public static final String BARBER_SERVICE_PRICE = "Ej: 12000";
        public static final String BARBER_SERVICE_INTERNAL_NOTES = "Notas internas para el personal (no visible para clientes)";
    }

    public static final class ClientPromptText {

        public static final String CLIENT_NATIONAL_ID_CARD_NUMBER = "Ej: 12345678";
        public static final String CLIENT_NAME = "Ej: Juan";
        public static final String CLIENT_SURNAME = "Ej: Pérez";
        public static final String CLIENT_EMAIL = "Ej: juan@gmail.com";
        public static final String CLIENT_PHONE_PROMPT_TEXT = "+54 9 1234 5678";
        public static final String CLIENT_OPTIONAL_NOTES = "Agrega notas sobre el cliente (alergias, preferencias, etc.)";
    }

    public static final class EmployeePromptText {

        public static final String EMPLOYEE_FIRST_NAME = "Ej: Juan";
        public static final String EMPLOYEE_LAST_NAME = "Ej: Perez";
        public static final String COMMISION_PERCENTAGE = "Ej: 60";
    }

    public static final class ProductPromptText {

        public static final String PRODUCT_NAME = "Ej: Cera para modelar";
        public static final String PRODUCT_OPTIONAL_DESCRIPTION = "Descripción opcional del producto";
        public static final String PRODUCT_BRAND = "Ej: American Crew";
        public static final String PRODUCT_SIZE_OR_VOLUME = "Ej: 250ml";
        public static final String PRICE_FIELD_DEFAULT_VALUE = "$0";
        public static final String DISCOUNT_PERCENTAGE_DEFAULT_VALUE = "0%";
        public static final String STOCK_LEVEL_DEFAULT_VALUE = "0 unidades";
    }

    public static final class SettingsPromptText {

        public static final String BARBER_SHOP_NAME = "Ej: La Tercera Barbershop";
        public static final String BARBER_SHOP_PHONE_NUMBER = "Ej: +54 9 1122334455";
        public static final String BARBER_SHOP_EMAIL = "Ej: latercera@barbershop.com";
        public static final String BARBER_SHOP_ADDRESS = "Ej: Calle 123";
        public static final String USER_ADMIN_NAME = "Ej: admin";
        public static final String USER_ADMIN_PASSWORD = "Mínimo 8 caracteres, al menos una mayúscula y un número.";
        public static final String USER_ADMIN_CONFIRMATION_PASSWORD = "Repetir contraseña";
    }
}
