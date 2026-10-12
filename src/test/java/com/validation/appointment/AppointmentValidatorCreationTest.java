package com.validation.appointment;

import com.abstract_test_class.BaseValidatorTest;
import com.config.preferences.AppPreferences;
import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.PublicAppointmentDTO;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentEndDateException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;

import static com.factory.AppointmentTestDataFactory.buildPublicAppointmentDTO;
import static com.factory.AppointmentTestDataFactory.buildValidAppointmentCreationDTO;
import static com.test_constant.AppointmentTestConstants.InvalidData.*;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.INSTANT;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.ZONE_ID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentValidatorCreationTest extends BaseValidatorTest<AppointmentValidator, AppointmentCreationDTO> {

    private final Clock clock = generateClockInstance(INSTANT, ZONE_ID);
    private final AppPreferences appPreferences = new AppPreferences();
    private PublicAppointmentDTO guestDTO;

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidAppointmentCreationDTO();
        guestDTO = buildPublicAppointmentDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new AppointmentValidator(validatorEngine, clock, appPreferences);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateForCreation(inputDTO);
        validator.validateForCreation(guestDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de cliente NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullClientID_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setClientID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullBarberServiceID_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setBarberserviceID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de empleado NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullEmployeeID_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEmployeeID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de inicio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullStartDatetime_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setStartDateTime(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullEndDatetime_WhenCreating_ThenThrows_ConstraintViolationException() {

        inputDTO.setEndDateTime(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de inicio anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidAppointmentStartDateException")
    void givenStartdatetimeBeforeCurrentDateTime_WhenCreating_ThenThrows_InvalidAppointmentStartDateException() {

        inputDTO.setStartDateTime(START_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentStartDateException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenEndDatetimeBeforeCurrentDatetime_WhenCreating_ThenThrows_InvalidAppointmentEndDateException() {

        inputDTO.setEndDateTime(END_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización anterior a la fecha y hora de inicio ingresadas, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenEndDatetimeBeforeStartdatetime_WhenCreating_ThenThrows_InvalidAppointmentEndDateException() {

        inputDTO.setEndDateTime(END_DATETIME_BEFORE_START_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de inicio anterior al horario de apertura, la validación deberá fallar y arrojará DatetimeOutsideServiceHoursException")
    void givenStartDatetimeBeforeOpeningHours_WhenCreating_ThenThrows_DatetimeOutsideServiceHoursException() {

        inputDTO.setStartDateTime(START_DATETIME_BEFORE_OPENING_TIME);
        inputDTO.setEndDateTime(END_DATETIME_FOR_OPENING_HOURS_TEST);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización posterior al horario de cierre, la validación deberá fallar y arrojará DateTimeOutsideServiceHoursException")
    void givenEndDatetimeAfterClosingHours_WhenCreating_ThenThrows_DateTimeOutsideServiceHoursException() {

        inputDTO.setEndDateTime(END_DATETIME_AFTER_CLOSING_TIME);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de creación de turno para invitado con primer nombre NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullFirstNameOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setGuestFirstName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con apellido del invitado NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullLastNameOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setGuestLastName(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con teléfono del invitado NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullPhoneOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setGuestPhone(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con fecha NULL, la validación fallará y arrojará NullPointerException")
    void givenNullDateOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setDate(null);

        assertThrows(NullPointerException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con hora de inicio NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullStartTimeOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setStartTime(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con ID de empleado NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullEmployeeIDOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setEmployeeID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con ID de servicio de barbería NULL, la validación fallará y arrojará ConstraintViolationException")
    void givenNullBarberServiceIDOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setBarberServiceID(null);

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con fecha en el pasado, la validación fallará y arrojará ConstraintViolationException")
    void givenPastDateOnGuestAppointment_ThenThrows_ConstraintViolationException() {

        guestDTO.setDate(LocalDate.now().minusDays(1));

        assertThrows(ConstraintViolationException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de turno público con fecha en el futuro lejano, la validación debe pasar")
    void givenFutureDateOnGuestAppointment_ThenValidationPasses() {

        guestDTO.setDate(LocalDate.now().plusDays(30));

        assertDoesNotThrow(() -> validator.validateForCreation(guestDTO));
    }
}