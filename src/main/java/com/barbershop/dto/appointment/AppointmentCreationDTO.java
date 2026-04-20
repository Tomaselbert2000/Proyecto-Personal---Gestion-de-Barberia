package com.barbershop.dto.appointment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static com.barbershop.launcher.constants.entity_constraints.AppointmentConstraintViolationMessageStarters.*;
import static com.barbershop.launcher.constants.entity_constraints.GenericConstraintViolationConstants.NOT_NULL;
import static com.barbershop.launcher.constants.entity_constraints.GenericConstraintViolationConstants.OPTIONAL_TEXT_MAX_SIZE;
import static com.barbershop.validation.common.CommonConstants.MAX_OPTIONAL_DESCRIPTION_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreationDTO {

    @NotNull(message = CLIENT_ID + NOT_NULL)
    private Long clientID;

    @NotNull(message = EMPLOYEE_ID + NOT_NULL)
    private Long employeeID;

    @NotNull(message = BARBER_SERVICE_ID + NOT_NULL)
    private Long barberserviceID;

    @NotNull(message = APPOINTMENT_START_DATETIME + NOT_NULL)
    private LocalDateTime startDateTime;

    @NotNull(message = APPOINTMENT_END_DATETIME + NOT_NULL)
    private LocalDateTime endDateTime;

    @NotNull(message = APPOINTMENT_OPTIONAL_NOTES + NOT_NULL)
    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = APPOINTMENT_OPTIONAL_NOTES + OPTIONAL_TEXT_MAX_SIZE)
    private String optionalNotes;
}
