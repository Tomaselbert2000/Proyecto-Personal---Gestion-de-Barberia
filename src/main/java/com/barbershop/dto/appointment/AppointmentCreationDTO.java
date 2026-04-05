package com.barbershop.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentCreationDTO {

    @NotNull
    private Long clientID;

    @NotNull
    private Long employeeID;

    @NotNull
    private Long barberserviceID;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;
}
