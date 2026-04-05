package com.barbershop.dto.employee;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.barbershop.utils.strings.RegexPatterns.NAME_REGEX;
import static com.barbershop.validation.common.CommonConstants.MAX_NAME_LENGTH;
import static com.barbershop.validation.common.CommonConstants.MIN_NAME_LENGTH;
import static com.barbershop.validation.employee.EmployeeValidatorConstants.MAX_COMISSION_PERCENTAGE_VALUE;
import static com.barbershop.validation.employee.EmployeeValidatorConstants.MIN_COMMISION_PERCENTAGE_VALUE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeUpdateDTO {

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String firstName;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String lastName;

    private Boolean isActive;
    private LocalDate terminationDate;

    @DecimalMin(MIN_COMMISION_PERCENTAGE_VALUE)
    @DecimalMax(MAX_COMISSION_PERCENTAGE_VALUE)
    private Double commissionPercentage;
}
