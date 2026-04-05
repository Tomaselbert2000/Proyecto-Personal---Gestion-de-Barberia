package com.barbershop.validation.employee;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private final Validator validatorEngine;

    public <T> void validateDTO(T dto) {

        checkIfDtoIsNull(dto);

        validateAnnotationConstraints(validatorEngine, dto);
    }
}