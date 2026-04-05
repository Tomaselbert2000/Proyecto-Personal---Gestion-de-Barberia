package com.barbershop.dto.employee;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeInfoDTO {

    private String firstName;
    private String lastName;
    private String hireDateAsString;
    private String terminationDateAsString;
}
