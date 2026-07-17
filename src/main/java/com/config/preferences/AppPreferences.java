package com.config.preferences;

import com.dto.settings.SettingsUpdateDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.prefs.Preferences;

/**
 * Componente encargado de gestionar las preferencias y configuraciones del BarberShop.
 * Proporciona métodos para obtener, establecer y guardar diferentes opciones como el tema,
 * información del barbero, horarios de apertura y cierre, notificaciones, etc.
 */

@Component
@Getter
@Setter
public class AppPreferences {

    private static final String APP_THEME = "app_theme";
    private static final String BARBER_SHOP_NAME = "barber_shop_name";
    private static final String BARBER_SHOP_ADDRESS = "barber_shop_address";
    private static final String BARBER_SHOP_PHONE_NUMBER = "barber_shop_phone_number";
    private static final String BARBER_SHOP_EMAIL = "barber_shop_email";
    private static final String BARBER_SHOP_OPENING_TIME = "barber_shop_opening_time";
    private static final String BARBER_SHOP_CLOSING_TIME = "barber_shop_closing_time";
    private static final String NEW_APPOINTMENT_NOTIFICATION = "new_appointment_notification";
    private static final String CLIENT_REMINDER_NOTIFICATION = "client_reminder_notification";
    private static final String LOW_STOCK_NOTIFICATION = "low_stock_notification";
    private static final String WORKPLACE_CHANGES_NOTIFICATION = "workplace_changes_notification";

    private static final String DEFAULT_THEME = "MD3_LIGHT";
    private static final String DEFAULT_STRING_VALUE = "";
    private static final Boolean DEFAULT_STATUS = true;
    private static final String DEFAULT_OPENING_TIME = "08:00";
    private static final String DEFAULT_CLOSING_TIME = "20:00";

    private static final Preferences prefs = Preferences.userNodeForPackage(AppPreferences.class);

    public String getTheme() {

        return prefs.get(APP_THEME, DEFAULT_THEME);
    }

    public void setTheme(String theme) {

        prefs.put(APP_THEME, theme == null || theme.isBlank() ? DEFAULT_STRING_VALUE : theme);
    }

    public String getBarberShopName() {

        return prefs.get(BARBER_SHOP_NAME, DEFAULT_STRING_VALUE);
    }

    public void setBarberShopName(String barberShopName) {

        prefs.put(BARBER_SHOP_NAME, barberShopName == null || barberShopName.isBlank() ? DEFAULT_STRING_VALUE : barberShopName);
    }

    public String getBarberShopAddress() {

        return prefs.get(BARBER_SHOP_ADDRESS, DEFAULT_STRING_VALUE);
    }

    public void setBarberShopAddress(String address) {

        prefs.put(BARBER_SHOP_ADDRESS, address == null || address.isBlank() ? DEFAULT_STRING_VALUE : address);
    }

    public String getBarberShopPhoneNumber() {

        return prefs.get(BARBER_SHOP_PHONE_NUMBER, DEFAULT_STRING_VALUE);
    }

    public void setBarberShopPhoneNumber(String phoneNumber) {

        prefs.put(BARBER_SHOP_PHONE_NUMBER, phoneNumber == null || phoneNumber.isBlank() ? DEFAULT_STRING_VALUE : phoneNumber);
    }

    public String getBarberShopEmail() {

        return prefs.get(BARBER_SHOP_EMAIL, DEFAULT_STRING_VALUE);
    }

    public void setBarberShopEmail(String email) {

        prefs.put(BARBER_SHOP_EMAIL, email == null || email.isBlank() ? DEFAULT_STRING_VALUE : email);
    }

    public String getBarberShopOpeningTime() {

        return prefs.get(BARBER_SHOP_OPENING_TIME, DEFAULT_OPENING_TIME);
    }

    public void setBarberShopOpeningTime(String openingTime) {

        prefs.put(BARBER_SHOP_OPENING_TIME, openingTime);
    }

    public String getBarberShopClosingTime() {

        return prefs.get(BARBER_SHOP_CLOSING_TIME, DEFAULT_CLOSING_TIME);
    }

    public void setBarberShopClosingTime(String closingTime) {

        prefs.put(BARBER_SHOP_CLOSING_TIME, closingTime);
    }

    public boolean isNewAppointmentNotificationEnabled() {

        return prefs.getBoolean(NEW_APPOINTMENT_NOTIFICATION, DEFAULT_STATUS);
    }

    public void setNewAppointmentNotificationEnabled(boolean status) {

        prefs.putBoolean(NEW_APPOINTMENT_NOTIFICATION, status);
    }

    public boolean isClientReminderNotificationEnabled() {

        return prefs.getBoolean(CLIENT_REMINDER_NOTIFICATION, DEFAULT_STATUS);
    }

    public void setClientReminderNotificationEnabled(boolean status) {

        prefs.putBoolean(CLIENT_REMINDER_NOTIFICATION, status);
    }

    public boolean isLowStockNotificationEnabled() {

        return prefs.getBoolean(LOW_STOCK_NOTIFICATION, DEFAULT_STATUS);
    }

    public void setLowStockNotificationEnabled(boolean status) {

        prefs.putBoolean(LOW_STOCK_NOTIFICATION, status);
    }

    public boolean isWorkplaceChangesNotificationEnabled() {

        return prefs.getBoolean(WORKPLACE_CHANGES_NOTIFICATION, DEFAULT_STATUS);
    }

    public void setWorkplaceChangesNotificationEnabled(boolean state) {

        prefs.putBoolean(WORKPLACE_CHANGES_NOTIFICATION, state);
    }

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