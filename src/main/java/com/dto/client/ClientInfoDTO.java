package com.dto.client;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientInfoDTO {

    private Long id;
    private String nationalIdentityCardNumber;
    private String firstName;
    private String lastName;
    private LocalDate registrationDate;
    private String email;
    private List<String> phoneNumbersList;
    private String optionalNotes;

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " DNI: " + this.nationalIdentityCardNumber;
    }
}
