package com.dto.appointment;

import com.enums.AppointmentStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static com.launcher.constants.ConstraintViolationMessages.AppointmentConstraintSubject.APPOINTMENT_OPTIONAL_NOTES;
import static com.validation.common.CommonConstants.MAX_OPTIONAL_DESCRIPTION_LENGTH;

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
