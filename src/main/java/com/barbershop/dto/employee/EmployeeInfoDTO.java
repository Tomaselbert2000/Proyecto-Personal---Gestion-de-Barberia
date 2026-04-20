package com.barbershop.dto.employee;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeInfoDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String hireDateAsString;
    private String terminationDateAsString;

    @Override
    public String toString() {

        return firstName + " " + lastName;
    }
}
