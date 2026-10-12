package com.dto.appointment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PublicAppointmentDTO {

    @NotBlank
    private String guestFirstName;

    @NotBlank
    private String guestLastName;

    @NotBlank
    private String guestPhone;

    @FutureOrPresent
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private Long employeeID;

    @NotNull
    private Long barberServiceID;
}
