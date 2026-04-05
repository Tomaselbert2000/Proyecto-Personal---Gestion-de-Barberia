package com.barbershop.validation.appointment;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.exceptions.appointment.DateTimeOutsideServiceHoursException;
import com.barbershop.exceptions.appointment.InvalidAppointmentEndDateException;
import com.barbershop.exceptions.appointment.InvalidAppointmentStartDateException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class AppointmentValidator {

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(20, 0);

    private final Clock clock;
    private final Validator validatorEngine;

    public void validateForCreation(AppointmentCreationDTO creationDTO) {

        checkIfDtoIsNull(creationDTO);

        validateAnnotationConstraints(validatorEngine, creationDTO);

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

            if (startDateTime.toLocalTime().isBefore(OPENING_TIME) || endDateTime.toLocalTime().isAfter(CLOSING_TIME))
                throw new DateTimeOutsideServiceHoursException();
        }
    }
}
