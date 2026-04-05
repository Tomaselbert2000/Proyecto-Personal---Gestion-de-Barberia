package com.barbershop.dto.appointment;

import com.barbershop.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

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
}
