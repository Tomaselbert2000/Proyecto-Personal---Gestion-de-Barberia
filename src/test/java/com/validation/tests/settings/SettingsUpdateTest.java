package com.validation.tests.settings;

import com.barbershop.dto.settings.SettingsUpdateDTO;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.settings.InvalidServiceHourException;
import com.barbershop.validation.settings.SettingsUpdateValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.SettingsUpdateDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SettingsUpdateTest implements ValidatorUpdateTestFunctions {

    private SettingsUpdateDTO settingsUpdateDTO;

    private final Validator validatorEngine = generateValidatorEngine();

    private final SettingsUpdateValidator settingsValidator = new SettingsUpdateValidator(validatorEngine);


    @Override
    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Override
    public void setupUpdateDTO() {

        settingsUpdateDTO = SettingsUpdateDTO.builder()
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

    @Test
    @DisplayName("Dado un DTO de actulización de ajustes NULL, la validación fallará y arrojará NullDTOException")
    void givenNullDTO_WhenUpdatingSettings_ThenThrows_NullDTOException() {

        settingsUpdateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dados cualquiera de los valores de ajustes ingresados como NULL, la validación no deberá fallar y no arrojará excepción")
    void givenAnySettingsFieldNull_WhenUpdatingSettings_ThenShouldNotThrowException() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un nombre de negocio con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidBusinessName_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        settingsUpdateDTO.setName(UPDATE_BARBER_SHOP_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un teléfono con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidPhoneNumber_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        settingsUpdateDTO.setPhone(UPDATE_BARBER_SHOP_INVALID_PHONE);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un email con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidEmail_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        settingsUpdateDTO.setEmail(UPDATE_BARBER_SHOP_INVALID_EMAIL);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dada una dirección con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidAddress_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        settingsUpdateDTO.setAddress(UPDATE_BARBER_SHOP_INVALID_ADDRESS);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dada una hora de apertura anterior al horario de ciere especificado, la validación fallará y arrojará InvalidServiceHoursException")
    void givenClosingTime_BeforeOpeningTime_WhenUpdatingSettings_ThenThrows_InvalidServiceHoursException() {

        settingsUpdateDTO.setClosingHour(UPDATE_OPENING_TIME.minusHours(1));

        assertThrows(InvalidServiceHourException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un nombre de negocio que supere la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenBusinessName_LargerThanMaxLength_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        settingsUpdateDTO.setName(UPDATE_BUSINESS_NAME_LONGER_THAN_MAX_LENGTH);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un teléfono que supere la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenPhoneNumber_LargerThanMaxLength_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        settingsUpdateDTO.setPhone(UPDATE_PHONE_NUMBER_LARGER_THAN_MAX_LENGTH);

        assertThrows(ConstraintViolationException.class, this::validateForUpdate);
    }

    @Override
    public void setAllFieldsOnNull() {

        settingsUpdateDTO.setName(null);
        settingsUpdateDTO.setPhone(null);
        settingsUpdateDTO.setEmail(null);
        settingsUpdateDTO.setAddress(null);
        settingsUpdateDTO.setOpeningHour(null);
        settingsUpdateDTO.setClosingHour(null);
        settingsUpdateDTO.setNewAppointmentNotificationEnabled(null);
        settingsUpdateDTO.setClientReminderNotificationEnabled(null);
        settingsUpdateDTO.setLowStockNotificationEnabled(null);
        settingsUpdateDTO.setWorkplaceChangesNotificationEnabled(null);
    }

    @Override
    public void validateForUpdate() {

        settingsValidator.validateDTO(settingsUpdateDTO);
    }
}
