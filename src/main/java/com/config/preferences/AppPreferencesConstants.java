package com.config.preferences;

/**
 * Constantes globales para la configuración de preferencias de la aplicación.
 *
 * <p>Este archivo centraliza todas las claves y valores predeterminados utilizados
 * en el sistema de persistencia de preferencias de la barbería. Proporciona un punto
 * único de referencia para evitar duplicación de constantes y facilita el mantenimiento
 * de la configuración del sistema.
 *
 * <p>Las constantes están organizadas en dos categorías:
 * <ul>
 *   <li>{@link AppPreferencesKey}: Claves de identificación para las preferencias</li>
 *   <li>{@link AppPreferencesDefaultValue}: Valores predeterminados para cada preferencia</li>
 * </ul>
 */
public final class AppPreferencesConstants {

    /**
     * Constantes que definen las claves de identificación para las preferencias de la aplicación.
     *
     * <p>Estas claves se utilizan como identificadores únicos en el sistema de persistencia
     * (por ejemplo, SharedPreferences, JSON, o base de datos) para almacenar y recuperar
     * configuraciones del usuario y datos de la barbería.
     */
    public static final class AppPreferencesKey {

        /**
         * Clave para almacenar el tema de la aplicación (MD3_LIGHT, MD3_DARK, etc.).
         */
        public static final String APP_THEME = "app_theme";

        /**
         * Clave para almacenar el nombre de la barbería.
         */
        public static final String BARBER_SHOP_NAME = "barber_shop_name";

        /**
         * Clave para almacenar la dirección física de la barbería.
         */
        public static final String BARBER_SHOP_ADDRESS = "barber_shop_address";

        /**
         * Clave para almacenar el número de teléfono de contacto de la barbería.
         */
        public static final String BARBER_SHOP_PHONE_NUMBER = "barber_shop_phone_number";

        /**
         * Clave para almacenar el correo electrónico de contacto de la barbería.
         */
        public static final String BARBER_SHOP_EMAIL = "barber_shop_email";

        /**
         * Clave para almacenar la hora de apertura programada de la barbería.
         */
        public static final String BARBER_SHOP_OPENING_TIME = "barber_shop_opening_time";

        /**
         * Clave para almacenar la hora de cierre programada de la barbería.
         */
        public static final String BARBER_SHOP_CLOSING_TIME = "barber_shop_closing_time";

        /**
         * Clave para controlar las notificaciones de nuevas citas agendadas.
         */
        public static final String NEW_APPOINTMENT_NOTIFICATION = "new_appointment_notification";

        /**
         * Clave para controlar las notificaciones de recordatorio a clientes.
         */
        public static final String CLIENT_REMINDER_NOTIFICATION = "client_reminder_notification";

        /**
         * Clave para controlar las notificaciones de stock bajo en inventario.
         */
        public static final String LOW_STOCK_NOTIFICATION = "low_stock_notification";

        /**
         * Clave para controlar las notificaciones sobre cambios en el lugar de trabajo.
         */
        public static final String WORKPLACE_CHANGES_NOTIFICATION = "workplace_changes_notification";

        /**
         * Clave para controlar si se deben recordar las credenciales de inicio de sesión.
         */
        public static final String REMEMBER_CREDENTIALS = "remember_credentials";

        /**
         * Clave para almacenar el identificador del usuario actual en sesión.
         */
        public static final String CURRENT_USER = "current_user";
    }

    /**
     * Constantes que definen los valores predeterminados para las preferencias de la aplicación.
     *
     * <p>Estos valores se utilizan como fallback cuando una preferencia no ha sido configurada
     * explícitamente por el usuario o administrador del sistema. Garantizan que la aplicación
     * funcione correctamente incluso sin configuración previa.
     */
    public static final class AppPreferencesDefaultValue {

        /**
         * Valor predeterminado para el tema de la aplicación: MD3_LIGHT.
         */
        public static final String DEFAULT_THEME = "MD3_LIGHT";

        /**
         * Valor predeterminado para campos de texto que no requieren contenido específico.
         */
        public static final String DEFAULT_STRING_VALUE = "";

        /**
         * Valor predeterminado para booleanos de estado (activado por defecto).
         */
        public static final Boolean DEFAULT_STATUS = true;

        /**
         * Valor predeterminado para recordar credenciales: false (seguridad por defecto).
         */
        public static final Boolean DEFAULT_REMEMBER_CREDENTIALS = false;

        /**
         * Hora predeterminada de apertura de la barbería
         */
        public static final String DEFAULT_OPENING_TIME = "08:00";

        /**
         * Hora predeterminada de cierre de la barbería
         */
        public static final String DEFAULT_CLOSING_TIME = "20:00";
    }
}