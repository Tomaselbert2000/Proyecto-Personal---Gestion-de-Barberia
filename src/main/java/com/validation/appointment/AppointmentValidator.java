package com.validation.appointment;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.exceptions.appointment.InvalidAppointmentEndDateException;
import com.exceptions.appointment.InvalidAppointmentStartDateException;
import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.validation.common.CommonValidationFunctions.checkIfDtoIsNull;

@Component
public class AppointmentValidator extends BaseDTOValidator {

    private final Clock clock;

    public AppointmentValidator(Validator validatorEngine, Clock clock) {
        super(validatorEngine);
        this.clock = clock;
    }

    public static final class AppointmentValidatorConstants {

        private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
        private static final LocalTime CLOSING_TIME = LocalTime.of(20, 0);
    }

    public void validateForCreation(AppointmentCreationDTO creationDTO) {

        super.validateDTO(creationDTO);

        validateDateTimeInterval(creationDTO.getStartDateTime(), creationDTO.getEndDateTime());
    }

    public void validateForUpdate(AppointmentUpdateDTO updateDTO) {

        checkIfDtoIsNull(updateDTO);

        validateDateTimeInterval(updateDTO.getNewStartDateTime(), updateDTO.getNewEndDateTime());
    }

    private void validateDateTimeInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        if (startDateTime != null && endDateTime != null) {

            LocalDateTime now = LocalDateTime.now(clock);

            if (startDateTime.isBefore(now)) throw new InvalidAppointmentStartDateException();

            if (endDateTime.isBefore(now) || endDateTime.isBefore(startDateTime))
                throw new InvalidAppointmentEndDateException();

            if (startDateTime.toLocalTime().isBefore(AppointmentValidatorConstants.OPENING_TIME) || endDateTime.toLocalTime().isAfter(AppointmentValidatorConstants.CLOSING_TIME))
                throw new DateTimeOutsideServiceHoursException();
        }
    }
}
