package com.presentation.constants;

import java.util.List;

public final class StringResource {

    private StringResource() {
    }

    public static final class OperationMessages {

        public static final String CREATION_SUCCESS_PREFIX = "Se ha registrado ";
        public static final String CREATION_SUCCESS_SUFFIX = " en el sistema.";

        public static final String CREATION_FAILED_PREFIX = "No se pudo registrar ";
        public static final String CREATION_FAILED_SUFFIX = ". Por favor, revisa lo siguiente: ";

        public static final String UPDATE_SUCCESS_PREFIX = "El ";
        public static final String UPDATE_SUCCESS_SUFFIX = " se ha actualizado exitosamente.";

        public static final String UPDATE_FAILED_PREFIX = "No se pudo actualizar ";
        public static final String UPDATE_FAILED_SUFFIX = ". Por favor, revisa lo siguiente: ";

        public static final String CLIENT_CREATION_SUCCESS = CREATION_SUCCESS_PREFIX + "un nuevo cliente" + CREATION_SUCCESS_SUFFIX;
        public static final String CLIENT_UPDATE_SUCCESS = UPDATE_SUCCESS_PREFIX + "cliente" + UPDATE_SUCCESS_SUFFIX;

        public static final String CLIENT_CREATION_FAILED = CREATION_FAILED_PREFIX + "el cliente" + CREATION_FAILED_SUFFIX;
        public static final String CLIENT_UPDATE_FAILED = UPDATE_FAILED_PREFIX + "el cliente" + UPDATE_FAILED_SUFFIX;

        public static final String EMPLOYEE_CREATION_FAILED = CREATION_FAILED_PREFIX + "el cliente" + CREATION_FAILED_SUFFIX;
        public static final String EMPLOYEE_UPDATE_FAILED = UPDATE_FAILED_PREFIX + "el empleado" + UPDATE_FAILED_SUFFIX;
    }

    public static final class ConfirmationDialog {
        public static final String CANCEL_BUTTON_TEXT = "Cancelar";
        public static final String CONFIRM_BUTTON_TEXT = "Confirmar";

        public static final String BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_TITLE = "Eliminar servicio del catálogo";
        public static final String BARBER_SERVICE_DELETE_CONFIRMATION_DIALOG_MESSAGE = "¿Está seguro que desea eliminar este servicio del catálogo? Esta acción es irreversible";
        public static final String BARBER_SERVICE_SUCCESSFULLY_DELETED_MESSAGE = "El servicio se ha eliminado exitosamente del catálogo.";
        public static final String BARBER_SERVICE_DELETION_FAILED_MESSAGE = "No es posible eliminar un servicio que tiene turnos históricos asociados.";

        public static final String CONFIRM_LOGOUT_DIALOG_TITLE = "Cerrar sesión";
        public static final String CONFIRM_LOGOUT_DIALOG_MESSAGE = "¿Está seguro que desea cerrar la sesión actual?";
    }

    public static final class DisplayString {
        public static final String APP_TITLE = "Sistema de gestión - La Tercera Barbershop";
        public static final String CURRENCY_STRING_ARG = "ARS $ ";
        public static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("*.png", "*.jpg");
        public static final String FILE_CHOOSER_IMAGE_DESCRIPTION = "Seleccionar una imagen";
        public static final String NEW_LINE = "\n• ";
        public static final String ACCEPT_BUTTON_TEXT = "Aceptar";
        public static final String ACTIVATE_BUTTON_TEXT = "Activar";
        public static final String DEACTIVATE_BUTTON_TEXT = "Desactivar";
        public static final String CLIPBOARD_BUTTON_TEXT = "Copiar al portapapeles";
        public static final String ACTIVE_STATUS_LABEL = "Activo";
        public static final String INACTIVE_STATUS_LABEL = "Inactivo";
        public static final String NO_DATA = "Sin datos";
        public static final String MIN_STOCK_LABEL_PREFIX = "Min: ";
        public static final String DEFAULT_TERMINATION_DATE_STRING = "-- : --";
    }

    public static final class StatMessageSuffix {
        public static final String COMPLETED = " completados";
        public static final String SCHEDULED_TOMORROW = " para mañana";
        public static final String VS_LAST_MONTH = " vs mes anterior";
        public static final String IN_THE_LAST_MONTH = " en el mes anterior";
        public static final String OUT_OF_A_TOTAL_OF = " de un total de ";
        public static final String OUT_OF_TOTAL = " del total";
        public static final String CATEGORIES = " categorías distintas";
        public static final String REGISTERED_SALES = " ventas realizadas";
        public static final String TOTAL_REVENUE = " recaudación total";
        public static final String TIMES_USED = " veces usado";
        public static final String NEW_THIS_MONTH = " nuevos este mes";
        public static final String NO_PHONE_REGISTERED = " sin teléfono";
    }

    public static final class StringFormat {
        public static final String PRICE_FORMAT = "%.2f";
        public static final String ONE_DECIMAL_FORMAT = "%.1f";
        public static final String PERCENTAGE_FORMAT = "%.1f%%";
    }

    public static final class EmptyListMessage {
        public static final String EMPTY_ACTIVITY_LOG_MESSAGE = "No se registra actividad reciente.";
        public static final String EMPTY_APPOINTMENTS_LIST_MESSAGE = "No se registran turnos agendados hasta este momento";
        public static final String EMPTY_BARBER_SERVICE_CATALOG_LIST_MESSAGE = "No se registran servicios en el catálogo.";
        public static final String EMPTY_CLIENT_LIST_MESSAGE = "No se registran clientes hasta el momento.";
        public static final String EMPTY_PRODUCT_LIST_MESSAGE = "No se registran productos en el sistema.";
        public static final String EMPTY_EMPLOYEE_LIST_MESSAGE = "No se registran empleados en el sistema hasta este momento.";
        public static final String EMPTY_PAYMENT_LIST_MESSAGE = "No se registran medios de pago hasta el momento";
    }

    public static final class ToastNotificationMessage {

        public static final String TOAST_NOTIFICATION_TITLE_SUCCESSFUL = "Operación exitosa";
        public static final String TOAST_NOTIFICATION_TITLE_FAILED = "Operación fallida";

        public static final String CLIENT_CREATION_TOAST_NOTIFICATION_MESSAGE = OperationMessages.CLIENT_CREATION_SUCCESS;
        public static final String CLIENT_EDITION_TOAST_NOTIFICATION_MESSAGE = OperationMessages.CLIENT_UPDATE_SUCCESS;

        public static final String EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo empleado en el sistema.";
        public static final String EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El empleado se ha actualizado exitosamente.";

        public static final String BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo servicio en el catálogo.";
        public static final String BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El servicio se ha actualizado exitosamente.";

        public static final String APPOINTMENT_CREATION_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo turno en la agenda.";
        public static final String APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE = "El estado de turno fue actualizado exitosamente.";

        public static final String PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo producto en el sistema.";
        public static final String PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El producto se ha actualizado exitosamente.";

        public static final String PAYMENT_METHOD_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo medio de pago en el sistema.";
        public static final String PAYMENT_METHOD_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El medio de pago fue actualizado exitosamente.";

        public static final String SETTINGS_UPDATE_NOTIFICATION_MESSAGE = "Los ajustes han sido actualizados correctamente.";
    }

    public static final class ValidationErrorMessage {
        public static final String VALIDATION_ERROR_TITLE = "Error de validación";

        public static final String LOGIN_ERROR_TITLE = "Error de inicio de sesión";
        public static final String LOGIN_FAILED = "Usuario o contraseña incorrectos";

        public static final String REGISTER_ERROR_TITLE = "Error de registro";
        public static final String REGISTER_VALIDATION_FAILED = OperationMessages.CREATION_FAILED_PREFIX + "el usuario. Por favor, revisa lo siguiente: "; // Simplificado
        public static final String PASSWORDS_DOES_NOT_MATCH = "Las contraseñas ingresadas no coinciden.";

        public static final String CLIENT_CREATION_VALIDATION_FAILED = OperationMessages.CLIENT_CREATION_FAILED;
        public static final String CLIENT_EDITION_VALIDATION_FAILED = OperationMessages.CLIENT_UPDATE_FAILED;

        public static final String PRODUCT_CREATION_VALIDATION_FAILED = "No se pudo registrar el producto. Por favor, revisa lo siguiente: ";
        public static final String PRODUCT_EDITION_VALIDATION_FAILED = "No se pudo actualizar el producto. Por favor, revisa lo siguiente: ";

        public static final String EMPLOYEE_CREATION_VALIDATION_FAILED = OperationMessages.EMPLOYEE_CREATION_FAILED; // Asumiendo que se define en OperationMessages
        public static final String EMPLOYEE_EDITION_VALIDATION_FAILED = OperationMessages.EMPLOYEE_UPDATE_FAILED;

        public static final String APPOINTMENT_CREATION_VALIDATION_FAILED = "No se pudo registrar el turno. Por favor, revisa lo siguiente: ";
        public static final String APPOINTMENT_EDITION_VALIDATION_FAILED = "No se pudo actualizar el turno. Por favor, revisa lo siguiente: ";

        public static final String BARBER_SERVICE_CREATION_VALIDATION_FAILED = "No se pudo registrar el servicio en el catálogo. Por favor, revisa lo siguiente: ";
        public static final String BARBER_SERVICE_UPDATE_VALIDATION_FAILED = "No se pudo actualizar el servicio en el catálogo. Por favor, revisa lo siguiente: ";

        public static final String PAYMENT_METHOD_CREATION_VALIDATION_FAILED = "No se pudo registrar el método de pago. Por favor, revisa lo siguiente:";
        public static final String PAYMENT_METHOD_EDITION_VALIDATION_FAILED = "No se pudo actualizar el método de pago. Por favor, revisa lo siguiente: ";

        public static final String SETTINGS_UPDATE_VALIDATION_FAILED = "Error al actualizar las preferencias del usuario. Por favor, revise lo siguiente: ";
        public static final String CREDENTIALS_UPDATE_VALIDATION_FAILED = "Error al actualizar las credenciales de usuario. Por favor, revise lo siguiente: ";
    }

    public static final class FxmlViewLoadingErrorMessage {

        private static final String ERROR_PREFIX = "Hubo un error al cargar la vista de ";

        public static final String LOGIN_VIEW_LOADING_FAILED = ERROR_PREFIX + "inicio de sesión.";
        public static final String REGISTER_VIEW_LOADING_FAILED = ERROR_PREFIX + "registro de usuario.";

        public static final String EMPLOYEE_VIEW_LOADING_FAILED = ERROR_PREFIX + "empleados.";
        public static final String EMPLOYEE_ITEM_VIEW_LOADING_FAILED = ERROR_PREFIX + "lista de empleados registrados.";
        public static final String EMPLOYEE_CREATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "registro de nuevo empleado.";
        public static final String EMPLOYEE_EDITION_VIEW_LOADING_FAILED = ERROR_PREFIX + "actualización de empleado.";

        public static final String BARBER_SERVICE_VIEW_LOADING_FAILED = ERROR_PREFIX + "servicios de barbería.";
        public static final String BARBER_SERVICE_CREATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "creación de servicio de barbería.";
        public static final String BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED = ERROR_PREFIX + "edición de servicio de barbería.";
        public static final String BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED = ERROR_PREFIX + "catálogo de servicios.";

        public static final String PRODUCTS_VIEW_LOADING_FAILED = ERROR_PREFIX + "productos.";
        public static final String PRODUCT_ITEM_VIEW_LOADING_FAILED = ERROR_PREFIX + "lista de pruductos registrados.";
        public static final String PRODUCT_CREATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "creación de productos.";
        public static final String PRODUCT_EDITION_VIEW_LOADING_FAILED = ERROR_PREFIX + "actualización de productos.";

        public static final String PAYMENT_METHOD_VIEW_LOADING_FAILED = ERROR_PREFIX + "métodos de pago.";
        public static final String PAYMENT_METHOD_CREATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "creación de métodos de pago.";
        public static final String PAYMENT_METHOD_EDITION_VIEW_LOADING_FAILED = ERROR_PREFIX + "lista de actualización de medios de pago.";
        public static final String PAYMENT_METHOD_ITEM_VIEW_LOADING_FAILED = ERROR_PREFIX + "lista de medios de pago registrados.";

        public static final String SETTINGS_VIEW_LOADING_FAILED = ERROR_PREFIX + "ajustes de la aplicación";

        public static final String RECENT_ACTIVITY_VIEW_LOADING_FAILED = ERROR_PREFIX + "actividad reciente.";

        public static final String CLIENTS_VIEW_LOADING_FAILED = ERROR_PREFIX + "clientes.";
        public static final String CLIENT_CREATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "registro de nuevo cliente.";
        public static final String CLIENT_EDITION_VIEW_LOADING_FAILED = ERROR_PREFIX + "actualización de cliente.";
        public static final String CLIENT_ITEM_VIEW_LOADING_FAILED = ERROR_PREFIX + "lista de clientes";

        public static final String APPOINTMENTS_VIEW_LOADING_FAILED = ERROR_PREFIX + "lista de turnos recientes.";
        public static final String APPOINTMENT_CREATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "creación de turnos.";
        public static final String APPOINTMENT_EDITION_VIEW_LOADING_FAILED = ERROR_PREFIX + "actualización de turnos.";

        public static final String TOAST_NOTIFICATION_VIEW_LOADING_FAILED = ERROR_PREFIX + "notificación emergente.";

        public static final String CONFIRMATION_DIALOG_VIEW_LOADING_FAILED = ERROR_PREFIX + "diálogo de confirmación.";
    }
}
