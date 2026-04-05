package com.validation.tests.appointment;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.barbershop.exceptions.appointment.InvalidAppointmentEndDateException;
import com.barbershop.exceptions.appointment.InvalidAppointmentStartDateException;
import com.barbershop.validation.appointment.AppointmentValidator;
import com.validation.common.ValidatorCreationTestFunctions;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;

import static com.barbershop.validation.common.CommonValidationFunctions.generateClockInstance;
import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.AppointmentTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppointmentValidatorCreationTest implements ValidatorCreationTestFunctions {

    private final Clock clock = generateClockInstance(INSTANT, ZONE_ID);

    private final Validator validatorEngine = generateValidatorEngine();

    private final AppointmentValidator validator = new AppointmentValidator(clock, validatorEngine);

    private AppointmentCreationDTO creationDTO;

    @BeforeEach
    public void init() {

        setupCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de cliente NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullClientID_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setClientID(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de servicio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullBarberServiceID_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setBarberserviceID(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un ID de empleado NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullEmployeeID_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setEmployeeID(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de inicio NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullStartDatetime_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setStartDateTime(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización NULL, la validación deberá fallar y arrojará ConstraintViolationException")
    void givenNullEndDatetime_WhenCreating_ThenThrows_ConstraintViolationException() {

        creationDTO.setEndDateTime(null);

        assertThrows(ConstraintViolationException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de inicio anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidAppointmentStartDateException")
    void givenStartdatetimeBeforeCurrentDateTime_WhenCreating_ThenThrows_InvalidAppointmentStartDateException() {

        creationDTO.setStartDateTime(START_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentStartDateException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenEndDatetimeBeforeCurrentDatetime_WhenCreating_ThenThrows_InvalidAppointmentEndDateException() {

        creationDTO.setEndDateTime(END_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización anterior a la fecha y hora de inicio ingresadas, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenEndDatetimeBeforeStartdatetime_WhenCreating_ThenThrows_InvalidAppointmentEndDateException() {

        creationDTO.setEndDateTime(END_DATETIME_BEFORE_START_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de inicio anterior al horario de apertura, la validación deberá fallar y arrojará DatetimeOutsideServiceHoursException")
    void givenStartDatetimeBeforeOpeningHours_WhenCreating_ThenThrows_DatetimeOutsideServiceHoursException() {

        creationDTO.setStartDateTime(START_DATETIME_BEFORE_OPENING_TIME);
        creationDTO.setEndDateTime(END_DATETIME_FOR_OPENING_HOURS_TEST);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateForCreation);
    }

    @Test
    @DisplayName("Dado un DTO de creación con una fecha y hora de finalización posterior al horario de cierre, la validación deberá fallar y arrojará DateTimeOutsideServiceHoursException")
    void givenEndDatetimeAfterClosingHours_WhenCreating_ThenThrows_DateTimeOutsideServiceHoursException() {

        creationDTO.setEndDateTime(START_DATETIME_FOR_CLOSING_HOURS_TEST);
        creationDTO.setEndDateTime(END_DATETIME_AFTER_CLOSING_TIME);
    }

    public void setupCreationDTO() {

        creationDTO = AppointmentCreationDTO.builder()
                .clientID(CLIENT_ID)
                .barberserviceID(BARBER_SERVICE_ID)
                .employeeID(EMPLOYEE_ID)
                .startDateTime(START_DATETIME)
                .endDateTime(END_DATETIME)
                .build();
    }

    public void validateForCreation() {

        validator.validateForCreation(creationDTO);
    }
}