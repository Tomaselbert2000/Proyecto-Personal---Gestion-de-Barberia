package com.validation.common;

import com.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

public abstract class BaseDTOValidator {

    protected final Validator validatorEngine;

    public BaseDTOValidator(Validator validatorEngine) {

        this.validatorEngine = validatorEngine;
    }

    public <T> void validateDTO(T dto) {

        checkIfDtoIsNull(dto);
        validateAnnotationConstraints(validatorEngine, dto);
    }

    protected <T> void checkIfDtoIsNull(T dto) {

        if (dto == null) throw new NullDTOException();
    }

    private <T> void validateAnnotationConstraints(Validator validatorEngine, T dto) {

        Set<ConstraintViolation<T>> constraintViolationSet = validatorEngine.validate(dto);

        if (!constraintViolationSet.isEmpty()) throw new ConstraintViolationException(constraintViolationSet);
    }
}
