package com.factory;

import com.dto.settings.SettingsUpdateDTO;

import static com.test_constant.SettingsUpdateTestConstants.UpdateValidData.*;

public class SettingsTestDataFactory {

    public static SettingsUpdateDTO buildValidSettingsUpdateDTO() {

        return SettingsUpdateDTO.builder()
                .name(UPDATE_BARBER_SHOP_NAME)
                .phone(UPDATE_BARBER_SHOP_PHONE)
                .email(UPDATE_BARBER_SHOP_EMAIL)
                .address(UPDATE_BARBER_SHOP_ADDRESS)
                .openingHour(UPDATE_OPENING_TIME)
                .closingHour(UPDATE_CLOSE_TIME)
                .newAppointmentNotificationEnabled(DEFAULT_UPDATE_CHECKBOX_STATUS)
                .clientReminderNotificationEnabled(DEFAULT_UPDATE_CHECKBOX_STATUS)
                .lowStockNotificationEnabled(DEFAULT_UPDATE_CHECKBOX_STATUS)
                .workplaceChangesNotificationEnabled(DEFAULT_UPDATE_CHECKBOX_STATUS)
                .build();
    }
}
