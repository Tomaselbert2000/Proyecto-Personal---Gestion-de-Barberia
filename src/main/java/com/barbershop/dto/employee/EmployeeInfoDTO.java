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
    private Double commissionPercentage;
    private Boolean isActive;

    @Override
    public String toString() {

        return firstName + " " + lastName;
    }
}
