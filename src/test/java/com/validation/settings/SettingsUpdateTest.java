package com.validation.settings;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.settings.SettingsUpdateDTO;
import com.exceptions.common.NullDTOException;
import com.exceptions.settings.InvalidServiceHourException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.SettingsTestDataFactory.buildValidSettingsUpdateDTO;
import static com.test_constant.SettingsUpdateTestConstants.InvalidData.*;
import static com.test_constant.SettingsUpdateTestConstants.UpdateValidData.UPDATE_OPENING_TIME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SettingsUpdateTest extends BaseValidatorTest<SettingsUpdateValidator, SettingsUpdateDTO> {

    @Test
    @DisplayName("Dado un DTO de actulización de ajustes NULL, la validación fallará y arrojará NullDTOException")
    void givenNullDTO_WhenUpdatingSettings_ThenThrows_NullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dados cualquiera de los valores de ajustes ingresados como NULL, la validación no deberá fallar y no arrojará excepción")
    void givenAnySettingsFieldNull_WhenUpdatingSettings_ThenShouldNotThrowException() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un nombre de negocio con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidBusinessName_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(UPDATE_BARBER_SHOP_INVALID_NAME);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un teléfono con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidPhoneNumber_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhone(UPDATE_BARBER_SHOP_INVALID_PHONE);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un email con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidEmail_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmail(UPDATE_BARBER_SHOP_INVALID_EMAIL);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dada una dirección con caractéres inválidos, la validación fallará y arrojará ConstraintViolationException")
    void givenInvalidAddress_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        inputDTO.setAddress(UPDATE_BARBER_SHOP_INVALID_ADDRESS);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dada una hora de apertura anterior al horario de ciere especificado, la validación fallará y arrojará InvalidServiceHoursException")
    void givenClosingTime_BeforeOpeningTime_WhenUpdatingSettings_ThenThrows_InvalidServiceHoursException() {

        inputDTO.setClosingHour(UPDATE_OPENING_TIME.minusHours(1));

        assertThrows(InvalidServiceHourException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un nombre de negocio que supere la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenBusinessName_LargerThanMaxLength_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        inputDTO.setName(UPDATE_BUSINESS_NAME_LONGER_THAN_MAX_LENGTH);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un teléfono que supere la longitud máxima, la validación fallará y arrojará ConstraintViolationException")
    void givenPhoneNumber_LargerThanMaxLength_WhenUpdatingSettings_ThenThrows_ConstraintViolationException() {

        inputDTO.setPhone(UPDATE_PHONE_NUMBER_LARGER_THAN_MAX_LENGTH);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setName(null);
        inputDTO.setPhone(null);
        inputDTO.setEmail(null);
        inputDTO.setAddress(null);
        inputDTO.setOpeningHour(null);
        inputDTO.setClosingHour(null);
        inputDTO.setNewAppointmentNotificationEnabled(null);
        inputDTO.setClientReminderNotificationEnabled(null);
        inputDTO.setLowStockNotificationEnabled(null);
        inputDTO.setWorkplaceChangesNotificationEnabled(null);
    }

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidSettingsUpdateDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new SettingsUpdateValidator(validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateDTO(inputDTO);
    }
}
