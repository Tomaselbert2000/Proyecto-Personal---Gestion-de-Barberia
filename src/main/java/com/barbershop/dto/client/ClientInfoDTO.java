package com.barbershop.dto.client;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientInfoDTO {

    String nationalIdentityCardNumber;
    String firstName;
    String lastName;
    LocalDate registrationDate;
    String optionalNotes;
}
