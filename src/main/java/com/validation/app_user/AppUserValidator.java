package com.validation.app_user;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class AppUserValidator {

    private final Validator validatorEngine;

    public <T> void validateDTO(T dto) {

        checkIfDtoIsNull(dto);

        validateAnnotationConstraints(validatorEngine, dto);
    }
}
