package com.validation.appointment;

import com.config.preferences.AppPreferences;
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

@Component
public class AppointmentValidator extends BaseDTOValidator {

    private final Clock clock;
    private final AppPreferences appPreferences;

    public AppointmentValidator(
            Validator validatorEngine,
            Clock clock,
            AppPreferences appPreferences
    ) {

        super(validatorEngine);
        this.clock = clock;
        this.appPreferences = appPreferences;
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

            if (startDateTime.isBefore(now())) throw new InvalidAppointmentStartDateException();

            if (endDateTime.isBefore(now()) || endDateTime.isBefore(startDateTime))
                throw new InvalidAppointmentEndDateException();

            if (startDateTime.toLocalTime().isBefore(currentOpeningTime()) || endDateTime.toLocalTime().isAfter(currentClosingTime()))
                throw new DateTimeOutsideServiceHoursException();
        }
    }

    private LocalDateTime now() {

        return LocalDateTime.now(clock);
    }

    private LocalTime currentOpeningTime() {

        return LocalTime.parse(appPreferences.getBarberShopOpeningTime());
    }

    private LocalTime currentClosingTime() {

        return LocalTime.parse(appPreferences.getBarberShopClosingTime());
    }
}
