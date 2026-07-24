package com.launcher.constants;

public final class ConstraintViolationMessages {

    private ConstraintViolationMessages() {
    }

    public static final class AppUserConstraintSubject {

        public static final String APP_USERNAME_STRING = "El nombre de usuario";
        public static final String APP_PASSWORD_STRING = "La contraseña";
    }

    public static final class AppointmentConstraintSubject {

        public static final String CLIENT_ID = "El ID de cliente";
        public static final String EMPLOYEE_ID = "El ID de empleado";
        public static final String BARBER_SERVICE_ID = "El ID de servicio del catálogo";
        public static final String APPOINTMENT_START_DATETIME = "La fecha y hora de inicio";
        public static final String APPOINTMENT_END_DATETIME = "La fecha y hora de finalización";
        public static final String APPOINTMENT_OPTIONAL_NOTES = "El campo de notas opcionales del turno";
    }

    public static final class BarberServiceConstraintSubject {

        public static final String BARBER_SERVICE_NAME = "El nombre de servicio para el catálogo";
        public static final String BARBER_SERVICE_PRICE = "El precio de servicio";
        public static final String BARBER_SERVICE_CATEGORY = "La categoría de servicio";
        public static final String BARBER_SERVICE_INTERNAL_NOTES = "El campo de notas opcionales de servicio";
    }

    public static final class ClientConstraintSubject {

        public static final String CLIENT_NATIONAL_ID_CARD_NUMBER = "El DNI de cliente";
        public static final String CLIENT_FIRST_NAME = "El nombre de cliente";
        public static final String CLIENT_LAST_NAME = "El apellido de cliente";
        public static final String CLIENT_EMAIL = "El correo electrónico de cliente";
        public static final String CLIENT_PHONE = "El/los teléfono/s de cliente";
        public static final String CLIENT_OPTIONAL_NOTES = "Las notas opcionales de cliente";
    }

    public static final class EmployeeConstraintSubject {

        public static final String EMPLOYEE_FIRST_NAME = "El nombre de empleado";
        public static final String EMPLOYEE_LAST_NAME = "El apellido de empleado";
        public static final String EMPLOYEE_HIRE_DATE = "La fecha de contratación";
        public static final String EMPLOYEE_COMMISSION_PERCENTAGE = "El valor de comisión";
    }

    public static final class PaymentMethodConstraintSubject {

        public static final String PAYMENT_METHOD_NAME = "El nombre de método de pago";
        public static final String PAYMENT_METHOD_DESCRIPTION = "La descripción del método de pago";
        public static final String PAYMENT_METHOD_MODIFIER_TYPE = "El tipo de modificador de precio del método de pago";
        public static final String PAYMENT_METHOD_PRICE_MODIFIER = "El valor de modificador de precio del método de pago";
    }

    public static final class ProductConstraintSubject {

        public static final String PRODUCT_NAME = "El nombre de producto";
        public static final String BRAND_NAME = "El nombre de la marca";
        public static final String OPTIONAL_DESCRIPTION = "La descripción opcional";
        public static final String PRESENTATION_UNIT = "La medida de presentación";
        public static final String PRESENTATION_SIZE = "El volumen ingresado";
        public static final String PRODUCT_COST = "El costo de producto";
        public static final String PRODUCT_MIN_PRICE = "El precio de venta mínimo permitido";
        public static final String PRODUCT_CURRENT_PRICE = "El precio actual de venta";
        public static final String PRODUCT_WHOLE_SALE_PRICE = "El precio de venta al por mayor";
        public static final String PRODUCT_DISCOUNT_VALUE = "El valor de descuento";
        public static final String PRODUCT_CATEGORY = "La categoría de producto";
        public static final String PRODUCT_CURRENT_STOCK_LEVEL = "El stock actual de producto";
        public static final String PRODUCT_SAFETY_STOCK_LEVEL = "El stock mínimo seguro";
        public static final String IMAGE_FILE_PATH = "La ruta de archivo";
    }

    public static final class SettingsConstraintSubject {

        public static final String SETTINGS_BARBER_SHOP_NAME = "El nombre de negocio";
        public static final String SETTINGS_BARBER_SHOP_PHONE = "El número de teléfono";
        public static final String SETTINGS_BARBER_SHOP_EMAIL = "El correo electrónico";
        public static final String SETTINGS_BARBER_SHOP_ADDRESS = "La dirección del local";
    }

    public static final class MessagePredicates {

        public static final String NOT_NULL = " no puede ser NULL";
        public static final String NOT_BLANK = " no puede quedar en blanco";
        public static final String INVALID_NAME_SIZE = " solo acepta un rango de caractéres entre 4 ~ 100";
        public static final String INVALID_PHONE_SIZE = " solo acepta un rango de 7 a 15 dígitos";
        public static final String OPTIONAL_TEXT_OR_DESCRIPTION_MAX_SIZE = " solo acepta un máximo de 256 caractéres";
        public static final String DOES_NOT_MATCH_NAME_REGEX = " solo acepta caractéres del alfabeto";
        public static final String DOES_NOT_MATCH_EMAIL_REGEX = " solo acepta caractéres del alfabeto, numéricos y especiales";
        public static final String DOES_NOT_MATCH_PHONE_REGEX = " solo acepta caractéres numéricos";
        public static final String DOES_NOT_MATCH_ADDRESS_REGEX = " solo acepta caractéres del alfabeto, numéricos y especiales";
        public static final String POSITIVE = " debe ser mayor a 0 (cero)";
        public static final String HIGHER_OR_EQUALS_THAN_ZERO = " debe ser igual o mayor a 0 (cero)";
        public static final String LOWER_OR_EQUALS_THAN_ONE = " debe ser menor o igual a 1 (uno)";
        public static final String PERCENTAGE_LOWER_THAN_ZERO = " debe ser igual o mayor a 0 (cero).";
        public static final String PERCENTAGE_HIGHER_THAN_100 = " debe ser igual o menor a 100 (cien).";
        public static final String CANNOT_BE_SPACES_ONLY = " no puede contener solo espacios";
    }
}
