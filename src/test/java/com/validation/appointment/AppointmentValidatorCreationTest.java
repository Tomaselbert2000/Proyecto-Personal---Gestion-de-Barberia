package com.validation.appointment;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.appointment.AppointmentCreationDTO;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentEndDateException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;

import static com.validation.common.CommonValidationFunctions.generateClockInstance;
import static com.factory.AppointmentTestDataFactory.buildValidAppointmentCreationDTO;
import static com.test_constant.AppointmentTestConstants.InvalidData.*;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.INSTANT;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.ZONE_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentValidatorCreationTest extends BaseValidatorTest<AppointmentValidator, AppointmentCreationDTO> {

    private final Clock clock = generateClockInstance(INSTANT, ZONE_ID);

    @Override
    protected void setupInputDTO() {

        inputDTO = buildValidAppointmentCreationDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new AppointmentValidator(clock, validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateForCreation(inputDTO);
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

        inputDTO.setEndDateTime(START_DATETIME_FOR_CLOSING_HOURS_TEST);
        inputDTO.setEndDateTime(END_DATETIME_AFTER_CLOSING_TIME);
    }
}