package com.launcher.constants;

import java.util.List;

public final class StringResource {

    private StringResource() {
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
        public static final String CLIPBOARD_BUTTON_TEXT = "Copiar al portapapeles";
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
        public static final String EMPTY_PRODUCT_LIST_MESSAGE = "No se registran productos en el sistema.";
        public static final String EMPTY_EMPLOYEE_LIST_MESSAGE = "No se registran empleados en el sistema hasta este momento.";
        public static final String EMPTY_PAYMENT_LIST_MESSAGE = "No se registran medios de pago hasta el momento";
    }

    public static final class ToastNotificationMessage {

        public static final String TOAST_NOTIFICATION_TITLE_SUCCESSFUL = "Operación exitosa";
        public static final String TOAST_NOTIFICATION_TITLE_FAILED = "Operación fallida";

        public static final String CLIENT_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo cliente en el sistema.";

        public static final String EMPLOYEE_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo empleado en el sistema.";
        public static final String EMPLOYEE_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El empleado se ha actualizado exitosamente.";

        public static final String BARBER_SERVICE_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo servicio en el catálogo.";
        public static final String BARBER_SERVICE_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El servicio se ha actualizado exitosamente.";

        public static final String APPOINTMENT_DATA_INCOMPLETE_NOTIFICATION_MESSAGE = "Campos de registro incompletos. Intente nuevamente.";
        public static final String APPOINTMENT_CREATION_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo turno en la agenda.";
        public static final String APPOINTMENT_STATUS_UPDATED_TOAST_NOTIFICATION_MESSAGE = "El estado de turno fue actualizado exitosamente.";

        public static final String PRODUCT_CREATION_TOAST_NOTIFICATION_MESSAGE = "Se ha registrado un nuevo producto en el sistema.";
        public static final String PRODUCT_UPDATE_TOAST_NOTIFICATION_MESSAGE = "El producto se ha actualizado exitosamente.";

        public static final String SETTINGS_UPDATE_NOTIFICATION_MESSAGE = "Los ajustes han sido actualizados correctamente.";
    }

    public static final class ValidationErrorMessage {

        public static final String VALIDATION_ERROR_TITLE = "Error de validación";

        public static final String LOGIN_ERROR_TITLE = "Error de inicio de sesión";
        public static final String LOGIN_FIELDS_BLANK = "Campos de ingreso en blanco.";
        public static final String LOGIN_FAILED = "Usuario o contraseña incorrectos";

        public static final String REGISTER_ERROR_TITLE = "Error de registro";
        public static final String REGISTER_VALIDATION_FAILED = "No se pudo registrar el usuario. Por favor, revisa lo siguiente: ";
        public static final String PASSWORDS_DOES_NOT_MATCH = "Las contraseñas ingresadas no coinciden.";

        public static final String CLIENT_CREATION_VALIDATION_FAILED = "No se pudo registrar el cliente. Por favor, revisa lo siguiente: ";

        public static final String PRODUCT_CREATION_VALIDATION_FAILED = "No se pudo registrar el producto. Por favor, revisa lo siguiente: ";
        public static final String PRODUCT_EDITION_VALIDATION_FAILED = "No se pudo actualizar el producto. Por favor, revisa lo siguiente: ";

        public static final String EMPLOYEE_CREATION_VALIDATION_FAILED = "No se pudo registrar el empleado. Por favor, revisa lo siguiente: ";
        public static final String EMPLOYEE_EDITION_VALIDATION_FAILED = "No se pudo actualizar el empleado. Por favor, revisa lo siguiente: ";

        public static final String APPOINTMENT_CREATION_VALIDATION_FAILED = "No se pudo registrar el turno. Por favor, revisa lo siguiente: ";
        public static final String APPOINTMENT_EDITION_VALIDATION_FAILED = "No se pudo actualizar el turno. Por favor, revisa lo siguiente: ";

        public static final String BARBER_SERVICE_CREATION_VALIDATION_FAILED = "No se pudo registrar el servicio en el catálogo. Por favor, revisa lo siguiente: ";

        public static final String SETTINGS_UPDATE_VALIDATION_FAILED = "Error al actualizar las preferencias del usuario. Por favor, revise lo siguiente: ";
        public static final String CREDENTIALS_UPDATE_VALIDATION_FAILED = "Error al actualizar las credenciales de usuario. Por favor, revise lo siguiente: ";
    }

    public static final class FxmlViewLoadingErrorMessage {

        public static final String LOGIN_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de inicio de sesión.";
        public static final String REGISTER_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de registro de usuario.";

        public static final String EMPLOYEE_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de empleados.";
        public static final String EMPLOYEE_ITEM_VIEW_LOADING_FAILED = "Hubo un error al cargar la lista de empleados registrados.";
        public static final String EMPLOYEE_CREATION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de registro de nuevo empleado.";
        public static final String EMPLOYEE_EDITION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de actualización de empleado.";

        public static final String BARBER_SERVICE_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de servicios de barbería.";
        public static final String BARBER_SERVICE_CREATION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de creación de servicio de barbería.";
        public static final String BARBER_SERVICE_EDITION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de edición de servicio de barbería.";
        public static final String BARBER_SERVICE_ITEM_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista del catálogo de servicios.";

        public static final String PRODUCTS_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de productos.";
        public static final String PRODUCT_ITEM_VIEW_LOADING_FAILED = "Hubo un error al cargar la lista de pruductos registrados.";
        public static final String PRODUCT_CREATION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de creación de productos.";
        public static final String PRODUCT_EDITION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de actualización de productos.";

        public static final String PAYMENT_METHOD_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de métodos de pago.";

        public static final String SETTINGS_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de ajustes de la aplicación";

        public static final String RECENT_ACTIVITY_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de actividad reciente.";

        public static final String CLIENTS_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de clientes.";
        public static final String CLIENT_CREATION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de registro de nuevo cliente.";

        public static final String APPOINTMENTS_VIEW_LOADING_FAILED = "Hubo un error al cargar la lista de turnos recientes.";
        public static final String APPOINTMENT_CREATION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de creación de turnos.";
        public static final String APPOINTMENT_EDITION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de actualización de turnos.";

        public static final String TOAST_NOTIFICATION_VIEW_LOADING_FAILED = "Hubo un error al cargar la vista de notificación emergente.";

        public static final String CONFIRMATION_DIALOG_VIEW_LOADING_FAILED = "Hubo un error al cargar el diálogo de confirmación.";
    }
}
