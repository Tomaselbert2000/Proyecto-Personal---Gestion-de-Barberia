package com.barbershop.dto.client;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientInfoDTO {

    private Long id;
    private String nationalIdentityCardNumber;
    private String firstName;
    private String lastName;
    private LocalDate registrationDate;
    private String optionalNotes;

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " DNI: " + this.nationalIdentityCardNumber;
    }
}
