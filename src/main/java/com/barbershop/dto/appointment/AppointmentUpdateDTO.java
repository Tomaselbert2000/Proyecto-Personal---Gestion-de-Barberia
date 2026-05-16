package com.barbershop.dto.appointment;

import com.barbershop.enums.AppointmentStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static com.barbershop.launcher.constants.entity_constraints.AppointmentConstraintViolationMessageSubject.APPOINTMENT_OPTIONAL_NOTES;
import static com.barbershop.validation.common.CommonConstants.MAX_OPTIONAL_DESCRIPTION_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentUpdateDTO {

    private Long newEmployeeID;

    private Long newBarberserviceID;

    private LocalDateTime newStartDateTime;

    private LocalDateTime newEndDateTime;

    private AppointmentStatus newStatus;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = APPOINTMENT_OPTIONAL_NOTES + MAX_OPTIONAL_DESCRIPTION_LENGTH)
    private String optionalNotes;
}
