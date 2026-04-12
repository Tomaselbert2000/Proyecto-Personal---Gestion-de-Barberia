package com.barbershop.dto.appointment;

import com.barbershop.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentInfoDTO {

    private Long id;
    private String clientFirstName;
    private String clientLastName;
    private String serviceName;
    private String employeeFirstName;
    private String employeeLastName;
    private LocalDateTime registrationTimestamp;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private AppointmentStatus currentStatus;
}
