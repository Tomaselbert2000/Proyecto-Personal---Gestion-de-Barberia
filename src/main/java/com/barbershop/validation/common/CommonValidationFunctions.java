package com.barbershop.validation.common;

import com.barbershop.exceptions.common.NullDTOException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;

public class CommonValidationFunctions {

    public static <T> void checkIfDtoIsNull(T dto) {

        if (dto == null) throw new NullDTOException();
    }

    public static <T> void validateAnnotationConstraints(Validator validatorEngine, T creationDTO) {

        Set<ConstraintViolation<T>> constraintViolationSet = validatorEngine.validate(creationDTO);

        if (!constraintViolationSet.isEmpty()) throw new ConstraintViolationException(constraintViolationSet);
    }

    public static Clock generateClockInstance(Instant instant, ZoneId zoneId) {

        return Clock.fixed(instant, zoneId);
    }

    public static Validator generateValidatorEngine() {

        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
