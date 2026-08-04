package com.validation.common;

import jakarta.validation.Validator;

import static com.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

public abstract class BaseDTOValidator {

    protected final Validator validatorEngine;

    public BaseDTOValidator(Validator validatorEngine) {

        this.validatorEngine = validatorEngine;
    }

    public <T> void validateDTO(T dto) {

        checkIfDtoIsNull(dto);
        validateAnnotationConstraints(validatorEngine, dto);
    }
}
