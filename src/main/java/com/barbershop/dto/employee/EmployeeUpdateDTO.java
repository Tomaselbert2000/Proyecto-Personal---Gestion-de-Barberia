package com.barbershop.dto.employee;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

import static com.barbershop.launcher.constants.entity_constraints.EmployeeConstraintViolationMessageSubject.*;
import static com.barbershop.launcher.constants.entity_constraints.ConstraintViolationMessagePredicate.*;
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

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = EMPLOYEE_FIRST_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = EMPLOYEE_FIRST_NAME + DOES_NOT_MATCH_REGEX)
    private String firstName;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = EMPLOYEE_LAST_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = EMPLOYEE_LAST_NAME + DOES_NOT_MATCH_REGEX)
    private String lastName;

    private Boolean isActive;
    private LocalDate terminationDate;

    @DecimalMin(value = MIN_COMMISION_PERCENTAGE_VALUE, message = EMPLOYEE_COMMISSION_PERCENTAGE + PERCENTAGE_LOWER_THAN_ZERO)
    @DecimalMax(value = MAX_COMISSION_PERCENTAGE_VALUE, message = EMPLOYEE_COMMISSION_PERCENTAGE + PERCENTAGE_HIGHER_THAN_100)
    private Double commissionPercentage;
}
