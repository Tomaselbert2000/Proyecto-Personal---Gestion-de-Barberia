package com.validation.tests.appointment;

import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.barbershop.exceptions.appointment.InvalidAppointmentEndDateException;
import com.barbershop.exceptions.appointment.InvalidAppointmentStartDateException;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.validation.appointment.AppointmentValidator;
import com.validation.common.ValidatorUpdateTestFunctions;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static com.barbershop.validation.common.CommonValidationFunctions.generateClockInstance;
import static com.barbershop.validation.common.CommonValidationFunctions.generateValidatorEngine;
import static com.validation.dataset.AppointmentTestDataset.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppointmentValidatorUpdateTest implements ValidatorUpdateTestFunctions {

    private final Clock clock = generateClockInstance(INSTANT, ZONE_ID);

    private final Validator validatorEngine = generateValidatorEngine();

    private final AppointmentValidator validator = new AppointmentValidator(clock, validatorEngine);

    private AppointmentUpdateDTO updateDTO;

    @BeforeEach
    public void init() {

        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación fallará y arrojará NullDTOException")
    void givenNullUpdateDTO_WhenUpdating_ThenThrowsNullDTOException() {

        updateDTO = null;

        assertThrows(NullDTOException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación deberá ser exitosa y se conservarán los valores actuales")
    void givenAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con fecha y hora de inicio anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidStartDateException")
    void givenNewStartDateTimeBeforeCurrentDateTime_WhenUpdating_ThenThrows_InvalidStartDateException() {

        updateDTO.setNewStartDateTime(START_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentStartDateException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de finalización anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenNewEndDateTimeBeforeCurrentDateTime_WhenUpdating_ThenThrows_InvalidAppointmentEndDateException() {

        updateDTO.setNewEndDateTime(END_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de finalización anterior a la fecha y hora de inicio ingresada, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenNewEndDateTimeBeforeNewStartDateTime_WhenUpdating_ThenThrows_InvalidAppointmentEndDateException() {

        updateDTO.setNewEndDateTime(END_DATETIME_BEFORE_START_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de inicio anterior al horario de apertura, la validación deberá fallar y arrojará DateTimeOutsideServiceHoursException")
    void givenNewStartTimeBeforeOpeningHours_WhenUpdating_ThenThrows_DateTimeOutsideServiceHoursException() {

        updateDTO.setNewStartDateTime(START_DATETIME_BEFORE_OPENING_TIME);
        updateDTO.setNewEndDateTime(END_DATETIME_FOR_OPENING_HOURS_TEST);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualizaicón con una fecha y hora de finalización posterior al horario de cierre, la validación deberá fallar y arrojará DateTimeOutsideServiceHoursException")
    void givenNewEndTimeAfterClosingHours_WhenUpdating_ThenThrows_DateTimeOutsideServiceHoursException() {

        updateDTO.setNewStartDateTime(START_DATETIME_FOR_CLOSING_HOURS_TEST);
        updateDTO.setNewEndDateTime(END_DATETIME_AFTER_CLOSING_TIME);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de inicio exactamente en el límite del horario de apertura, la validación deberá ser exitosa y no arrojará excepción")
    void givenNewStartTimeExactlyOnOpeningHours_WhenUpdating_ThenDoesNotThrowAnything() {

        updateDTO.setNewStartDateTime(START_DATETIME_EXACTLY_ON_OPENING_HOURS);
        updateDTO.setNewEndDateTime(END_DATETIME_FOR_EXACTLY_OPENING_HOURS_TEST);

        assertDoesNotThrow(this::validateForUpdate);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de finalización en el límite del horario de cierre, la validación deberá ser exitosa y no arrojará excepción")
    void givenNewEndTimeExactlyOnClosingHours_WhenUpdating_ThenDoesNotThrowsAnything() {

        updateDTO.setNewStartDateTime(START_DATETIME_FOR_EXACTLY_CLOSING_HOURS_TEST);
        updateDTO.setNewEndDateTime(END_DATETIME_EXACTLY_ON_CLOSING_HOURS);

        assertDoesNotThrow(this::validateForUpdate);
    }

    public void setupUpdateDTO() {

        updateDTO = AppointmentUpdateDTO.builder()
                .newEmployeeID(NEW_EMPLOYEE_ID)
                .newBarberserviceID(NEW_BARBER_SERVICE_ID)
                .newStartDateTime(START_DATETIME)
                .newEndDateTime(END_DATETIME)
                .newStatus(NEW_STATUS)
                .build();
    }

    public void validateForUpdate() {

        validator.validateForUpdate(updateDTO);
    }

    public void setAllFieldsOnNull() {

        updateDTO.setNewEmployeeID(null);
        updateDTO.setNewBarberserviceID(null);
        updateDTO.setNewStartDateTime(null);
        updateDTO.setNewEndDateTime(null);
        updateDTO.setNewStatus(null);
    }
}
