package com.validation.appointment;

import com.abstract_test_class.BaseValidatorTest;
import com.dto.appointment.AppointmentUpdateDTO;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentEndDateException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.exceptions.common.NullDTOException;
import com.factory.AppointmentTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static com.test_constant.AppointmentTestConstants.InvalidData.*;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.INSTANT;
import static com.test_constant.AppointmentTestConstants.TestTimeConfigurationData.ZONE_ID;
import static com.validation.common.CommonValidationFunctions.generateClockInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AppointmentValidatorUpdateTest extends BaseValidatorTest<AppointmentValidator, AppointmentUpdateDTO> {

    private final Clock clock = generateClockInstance(INSTANT, ZONE_ID);

    @Override
    protected void setupInputDTO() {

        inputDTO = AppointmentTestDataFactory.buildValidAppointmentUpdateDTO();
    }

    @Override
    protected void setupValidator() {

        validator = new AppointmentValidator(clock, validatorEngine);
    }

    @Override
    protected void validateInputDTO() {

        validator.validateForUpdate(inputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, la validación fallará y arrojará NullDTOException")
    void givenNullUpdateDTO_WhenUpdating_ThenThrowsNullDTOException() {

        inputDTO = null;

        assertThrows(NullDTOException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con todos sus atributos NULL, la validación deberá ser exitosa y se conservarán los valores actuales")
    void givenAllFieldsNull_WhenUpdating_ThenDoesNotThrowAnything() {

        setAllFieldsOnNull();

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con fecha y hora de inicio anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidStartDateException")
    void givenNewStartDateTimeBeforeCurrentDateTime_WhenUpdating_ThenThrows_InvalidStartDateException() {

        inputDTO.setNewStartDateTime(START_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentStartDateException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de finalización anterior a la fecha y hora actuales, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenNewEndDateTimeBeforeCurrentDateTime_WhenUpdating_ThenThrows_InvalidAppointmentEndDateException() {

        inputDTO.setNewEndDateTime(END_DATETIME_BEFORE_CURRENT_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de finalización anterior a la fecha y hora de inicio ingresada, la validación deberá fallar y arrojará InvalidAppointmentEndDateException")
    void givenNewEndDateTimeBeforeNewStartDateTime_WhenUpdating_ThenThrows_InvalidAppointmentEndDateException() {

        inputDTO.setNewEndDateTime(END_DATETIME_BEFORE_START_DATETIME);

        assertThrows(InvalidAppointmentEndDateException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de inicio anterior al horario de apertura, la validación deberá fallar y arrojará DateTimeOutsideServiceHoursException")
    void givenNewStartTimeBeforeOpeningHours_WhenUpdating_ThenThrows_DateTimeOutsideServiceHoursException() {

        inputDTO.setNewStartDateTime(START_DATETIME_BEFORE_OPENING_TIME);
        inputDTO.setNewEndDateTime(END_DATETIME_FOR_OPENING_HOURS_TEST);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualizaicón con una fecha y hora de finalización posterior al horario de cierre, la validación deberá fallar y arrojará DateTimeOutsideServiceHoursException")
    void givenNewEndTimeAfterClosingHours_WhenUpdating_ThenThrows_DateTimeOutsideServiceHoursException() {

        inputDTO.setNewStartDateTime(START_DATETIME_FOR_CLOSING_HOURS_TEST);
        inputDTO.setNewEndDateTime(END_DATETIME_AFTER_CLOSING_TIME);

        assertThrows(DateTimeOutsideServiceHoursException.class, this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de inicio exactamente en el límite del horario de apertura, la validación deberá ser exitosa y no arrojará excepción")
    void givenNewStartTimeExactlyOnOpeningHours_WhenUpdating_ThenDoesNotThrowAnything() {

        inputDTO.setNewStartDateTime(START_DATETIME_EXACTLY_ON_OPENING_HOURS);
        inputDTO.setNewEndDateTime(END_DATETIME_FOR_EXACTLY_OPENING_HOURS_TEST);

        assertDoesNotThrow(this::validateInputDTO);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una fecha y hora de finalización en el límite del horario de cierre, la validación deberá ser exitosa y no arrojará excepción")
    void givenNewEndTimeExactlyOnClosingHours_WhenUpdating_ThenDoesNotThrowsAnything() {

        inputDTO.setNewStartDateTime(START_DATETIME_FOR_EXACTLY_CLOSING_HOURS_TEST);
        inputDTO.setNewEndDateTime(END_DATETIME_EXACTLY_ON_CLOSING_HOURS);

        assertDoesNotThrow(this::validateInputDTO);
    }

    public void setAllFieldsOnNull() {

        inputDTO.setNewEmployeeID(null);
        inputDTO.setNewBarberserviceID(null);
        inputDTO.setNewStartDateTime(null);
        inputDTO.setNewEndDateTime(null);
        inputDTO.setNewStatus(null);
    }
}
