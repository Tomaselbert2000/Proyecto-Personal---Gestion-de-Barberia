package com.config.preferences;

import com.dto.settings.SettingsUpdateDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.prefs.Preferences;

import static com.config.preferences.AppPreferencesConstants.AppPreferencesDefaultValue.*;
import static com.config.preferences.AppPreferencesConstants.AppPreferencesKey.*;

/**
 * Gestor central de preferencias de aplicación que encapsula la persistencia de configuración
 * del usuario y parámetros del negocio mediante la API Preferences del JDK.
 *
 * <p>Este componente se integra en la arquitectura Spring como bean inyectable, proporcionando
 * una capa de abstracción sobre el almacenamiento persistente del sistema operativo. Gestiona
 * preferencias de sesión (usuario actual), configuración del negocio (nombre, dirección, horarios)
 * y configuraciones de notificaciones del sistema.</p>
 *
 * <h3>Responsabilidades</h3>
 * <ul>
 *   <li>Manejo de preferencias de usuario persistentes entre sesiones</li>
 *   <li>Validación de valores nulos o vacíos antes de persistir</li>
 *   <li>Aplicación de valores por defecto cuando no existen en la persistencia</li>
 *   <li>Centralización de configuración para evitar dispersión en el código</li>
 * </ul>
 *
 * <h3>Consideraciones de Arquitectura</h3>
 * <p>Utiliza {@link Preferences} del JDK que proporciona almacenamiento persistente por usuario.
 * Las preferencias se guardan en archivos XML en el sistema operativo, sobreviviendo a reinicios
 * de la aplicación. El componente es singleton por defecto en Spring.</p>
 */
@Component
@Getter
@Setter
public class AppPreferences {

    /**
     * Referencia estática al nodo de preferencias del usuario asociado al paquete de esta clase.
     * Proporciona acceso persistente a la configuración sin necesidad de crear nuevas instancias.
     */
    private static final Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);

    /**
     * Obtiene el nombre del usuario actualmente autenticado en el sistema.
     *
     * @return El nombre del usuario actual, o el valor por defecto si no existe en la persistencia
     * o si el usuario no está autenticado. Nunca retorna null.
     */
    public String getCurrentUser() {

        return prefs.get(CURRENT_USER, DEFAULT_STRING_VALUE);
    }

    /**
     * Establece el nombre del usuario actual en la persistencia de preferencias.
     * Valida que el valor no sea null ni esté vacío, aplicando el valor por defecto
     * en caso contrario para mantener la integridad de los datos.
     *
     * @param currentUser El nombre del usuario a guardar. No debe ser null ni estar vacío.
     */
    public void setCurrentUser(String currentUser) {

        prefs.put(CURRENT_USER, currentUser == null || currentUser.isBlank() ? DEFAULT_STRING_VALUE : currentUser);
    }

    /**
     * Verifica si la funcionalidad de recordatorio de credenciales está habilitada en la configuración del usuario.
     *
     * @return true si el usuario desea recordar credenciales, false si no. Retorna el valor por defecto
     * si la preferencia no existe en la persistencia.
     */
    public boolean isRememberCredentialsEnabled() {

        return prefs.getBoolean(REMEMBER_CREDENTIALS, DEFAULT_REMEMBER_CREDENTIALS);
    }

    /**
     * Habilita o deshabilita el recordatorio de credenciales para el usuario actual.
     *
     * @param status Estado booleano que indica si la funcionalidad está activa (true) o inactiva (false).
     */
    public void setRememberCredentials(boolean status) {

        prefs.putBoolean(REMEMBER_CREDENTIALS, status);
    }

    /**
     * Obtiene el tema de interfaz gráfica seleccionado por el usuario.
     *
     * @return El nombre del tema actual (ej: "dark", "light", "system"), o el valor por defecto
     * si no existe en la persistencia. Nunca retorna null.
     */
    public String getTheme() {

        return prefs.get(APP_THEME, DEFAULT_THEME);
    }

    /**
     * Establece el tema de interfaz gráfica para la aplicación.
     * Valida que el valor no sea null ni esté vacío, aplicando el valor por defecto
     * en caso contrario para evitar temas inválidos.
     *
     * @param theme El nombre del tema a aplicar. No debe ser null ni estar vacío.
     */
    public void setTheme(String theme) {

        prefs.put(APP_THEME, theme == null || theme.isBlank() ? DEFAULT_STRING_VALUE : theme);
    }

    /**
     * Obtiene el nombre comercial de la barbería configurado en las preferencias del sistema.
     *
     * @return El nombre de la barbería, o el valor por defecto si no existe en la persistencia.
     * Nunca retorna null.
     */
    public String getBarberShopName() {

        return prefs.get(BARBER_SHOP_NAME, DEFAULT_STRING_VALUE);
    }

    /**
     * Establece el nombre comercial de la barbería en la configuración del sistema.
     * Valida que el valor no sea null ni esté vacío, aplicando el valor por defecto
     * en caso contrario para mantener la consistencia de los datos empresariales.
     *
     * @param barberShopName El nombre de la barbería a guardar. No debe ser null ni estar vacío.
     */
    public void setBarberShopName(String barberShopName) {

        prefs.put(BARBER_SHOP_NAME, barberShopName == null || barberShopName.isBlank() ? DEFAULT_STRING_VALUE : barberShopName);
    }

    /**
     * Obtiene la dirección física de la barbería almacenada en las preferencias del sistema.
     *
     * @return La dirección completa de la barbería, o el valor por defecto si no existe en la persistencia.
     * Nunca retorna null.
     */
    public String getBarberShopAddress() {

        return prefs.get(BARBER_SHOP_ADDRESS, DEFAULT_STRING_VALUE);
    }

    /**
     * Establece la dirección física de la barbería en la configuración del sistema.
     * Valida que el valor no sea null ni esté vacío, aplicando el valor por defecto
     * en caso contrario para evitar direcciones inválidas en registros empresariales.
     *
     * @param address La dirección de la barbería a guardar. No debe ser null ni estar vacío.
     */
    public void setBarberShopAddress(String address) {

        prefs.put(BARBER_SHOP_ADDRESS, address == null || address.isBlank() ? DEFAULT_STRING_VALUE : address);
    }

    /**
     * Obtiene el número de teléfono de contacto de la barbería almacenado en las preferencias.
     *
     * @return El número de teléfono de la barbería, o el valor por defecto si no existe en la persistencia.
     * Nunca retorna null.
     */
    public String getBarberShopPhoneNumber() {

        return prefs.get(BARBER_SHOP_PHONE_NUMBER, DEFAULT_STRING_VALUE);
    }

    /**
     * Establece el número de teléfono de contacto de la barbería en la configuración del sistema.
     * Valida que el valor no sea null ni esté vacío, aplicando el valor por defecto
     * en caso contrario para mantener datos de contacto válidos.
     *
     * @param phoneNumber El número de teléfono a guardar. No debe ser null ni estar vacío.
     */
    public void setBarberShopPhoneNumber(String phoneNumber) {

        prefs.put(BARBER_SHOP_PHONE_NUMBER, phoneNumber == null || phoneNumber.isBlank() ? DEFAULT_STRING_VALUE : phoneNumber);
    }

    /**
     * Obtiene la dirección de correo electrónico de contacto de la barbería almacenada en las preferencias.
     *
     * @return La dirección de correo electrónico de la barbería, o el valor por defecto si no existe en la persistencia.
     * Nunca retorna null.
     */
    public String getBarberShopEmail() {

        return prefs.get(BARBER_SHOP_EMAIL, DEFAULT_STRING_VALUE);
    }

    /**
     * Establece la dirección de correo electrónico de contacto de la barbería en la configuración del sistema.
     * Valida que el valor no sea null ni esté vacío, aplicando el valor por defecto
     * en caso contrario para mantener datos de contacto válidos.
     *
     * @param email La dirección de correo electrónico a guardar. No debe ser null ni estar vacío.
     */
    public void setBarberShopEmail(String email) {

        prefs.put(BARBER_SHOP_EMAIL, email == null || email.isBlank() ? DEFAULT_STRING_VALUE : email);
    }

    /**
     * Obtiene el horario de apertura de la barbería configurado en las preferencias del sistema.
     *
     * @return El horario de apertura en formato string (ej: "09:00", "10:00"), o el valor por defecto
     * si no existe en la persistencia. Nunca retorna null.
     */
    public String getBarberShopOpeningTime() {

        return prefs.get(BARBER_SHOP_OPENING_TIME, DEFAULT_OPENING_TIME);
    }

    /**
     * Establece el horario de apertura de la barbería en la configuración del sistema.
     * No aplica validación adicional ya que el valor se asume válido desde la capa de negocio.
     *
     * @param openingTime El horario de apertura a guardar en formato string.
     */
    public void setBarberShopOpeningTime(String openingTime) {

        prefs.put(BARBER_SHOP_OPENING_TIME, openingTime);
    }

    /**
     * Obtiene el horario de cierre de la barbería configurado en las preferencias del sistema.
     *
     * @return El horario de cierre en formato string (ej: "20:00", "21:00"), o el valor por defecto
     * si no existe en la persistencia. Nunca retorna null.
     */
    public String getBarberShopClosingTime() {

        return prefs.get(BARBER_SHOP_CLOSING_TIME, DEFAULT_CLOSING_TIME);
    }

    /**
     * Establece el horario de cierre de la barbería en la configuración del sistema.
     * No aplica validación adicional ya que el valor se asume válido desde la capa de negocio.
     *
     * @param closingTime El horario de cierre a guardar en formato string.
     */
    public void setBarberShopClosingTime(String closingTime) {

        prefs.put(BARBER_SHOP_CLOSING_TIME, closingTime);
    }

    /**
     * Verifica si las notificaciones para nuevos citas están habilitadas en la configuración del usuario.
     *
     * @return true si las notificaciones de nuevas citas están activas, false si no. Retorna el valor
     * por defecto si la preferencia no existe en la persistencia.
     */
    public boolean isNewAppointmentNotificationEnabled() {

        return prefs.getBoolean(NEW_APPOINTMENT_NOTIFICATION, DEFAULT_STATUS);
    }

    /**
     * Habilita o deshabilita las notificaciones para nuevos citas del sistema.
     *
     * @param status Estado booleano que indica si las notificaciones están activas (true) o inactivas (false).
     */
    public void setNewAppointmentNotificationEnabled(boolean status) {

        prefs.putBoolean(NEW_APPOINTMENT_NOTIFICATION, status);
    }

    /**
     * Verifica si las notificaciones de recordatorio para clientes están habilitadas en la configuración del usuario.
     *
     * @return true si las notificaciones de recordatorio para clientes están activas, false si no. Retorna el valor
     * por defecto si la preferencia no existe en la persistencia.
     */
    public boolean isClientReminderNotificationEnabled() {

        return prefs.getBoolean(CLIENT_REMINDER_NOTIFICATION, DEFAULT_STATUS);
    }

    /**
     * Habilita o deshabilita las notificaciones de recordatorio para clientes del sistema.
     *
     * @param status Estado booleano que indica si las notificaciones están activas (true) o inactivas (false).
     */
    public void setClientReminderNotificationEnabled(boolean status) {

        prefs.putBoolean(CLIENT_REMINDER_NOTIFICATION, status);
    }

    /**
     * Verifica si las notificaciones de stock bajo están habilitadas en la configuración del usuario.
     *
     * @return true si las notificaciones de stock bajo están activas, false si no. Retorna el valor
     * por defecto si la preferencia no existe en la persistencia.
     */
    public boolean isLowStockNotificationEnabled() {

        return prefs.getBoolean(LOW_STOCK_NOTIFICATION, DEFAULT_STATUS);
    }

    /**
     * Habilita o deshabilita las notificaciones de stock bajo del sistema.
     *
     * @param status Estado booleano que indica si las notificaciones están activas (true) o inactivas (false).
     */
    public void setLowStockNotificationEnabled(boolean status) {

        prefs.putBoolean(LOW_STOCK_NOTIFICATION, status);
    }

    /**
     * Verifica si las notificaciones de cambios en el lugar de trabajo están habilitadas en la configuración del usuario.
     *
     * @return true si las notificaciones de cambios en el lugar de trabajo están activas, false si no. Retorna el valor
     * por defecto si la preferencia no existe en la persistencia.
     */
    public boolean isWorkplaceChangesNotificationEnabled() {

        return prefs.getBoolean(WORKPLACE_CHANGES_NOTIFICATION, DEFAULT_STATUS);
    }

    /**
     * Habilita o deshabilita las notificaciones de cambios en el lugar de trabajo del sistema.
     *
     * @param state Estado booleano que indica si las notificaciones están activas (true) o inactivas (false).
     */
    public void setWorkplaceChangesNotificationEnabled(boolean state) {

        prefs.putBoolean(WORKPLACE_CHANGES_NOTIFICATION, state);
    }

    /**
     * Persiste múltiples configuraciones de la barbería y preferencias del usuario en una sola operación.
     * Este metodo centraliza la actualización de configuración desde un DTO de actualización,
     * reduciendo la dispersión de llamadas individuales a métodos setters.
     *
     * <h3>Flujo de Ejecución</h3>
     * <ol>
     *   <li>Extrae todos los valores del {@link SettingsUpdateDTO} proporcionado</li>
     *   <li>Aplica cada valor a su correspondiente preferencia en el sistema</li>
     *   <li>Para valores de horario, convierte objetos Time a formato string</li>
     *   <li>Mantiene la integridad de los datos aplicando validaciones en setters individuales</li>
     * </ol>
     *
     * <h3>Consideraciones de Diseño</h3>
     * <p>Este metodo implementa el patrón de actualización por DTO, permitiendo que la capa de
     * controlador o servicio envíe toda la configuración en una sola llamada. Los setters individuales
     * encapsulan la lógica de validación, manteniendo este metodo limpio y legible.</p>
     *
     * @param settingsUpdateDTO Objeto contenedor con todos los valores de configuración a persistir.
     *                          Debe contener valores válidos según las reglas de negocio definidas en el DTO.
     */
    public void saveSettings(SettingsUpdateDTO settingsUpdateDTO) {

        setTheme(settingsUpdateDTO.getThemeSelected());
        setBarberShopName(settingsUpdateDTO.getName());
        setBarberShopPhoneNumber(settingsUpdateDTO.getPhone());
        setBarberShopEmail(settingsUpdateDTO.getEmail());
        setBarberShopAddress(settingsUpdateDTO.getAddress());

        setBarberShopOpeningTime(settingsUpdateDTO.getOpeningHour().toString());
        setBarberShopClosingTime(settingsUpdateDTO.getClosingHour().toString());

        setNewAppointmentNotificationEnabled(settingsUpdateDTO.getNewAppointmentNotificationEnabled());
        setClientReminderNotificationEnabled(settingsUpdateDTO.getClientReminderNotificationEnabled());
        setLowStockNotificationEnabled(settingsUpdateDTO.getLowStockNotificationEnabled());
        setWorkplaceChangesNotificationEnabled(settingsUpdateDTO.getWorkplaceChangesNotificationEnabled());
    }
}