package com.abstract_test_class;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
public abstract class BaseValidatorTest<V, D> {

    protected final Validator validatorEngine = generateValidatorEngine();

    protected V validator;
    protected D inputDTO;

    @BeforeEach
    void init() {

        setupInputDTO();
        setupValidator();
    }

    protected abstract void setupInputDTO();

    protected abstract void setupValidator();

    protected abstract void validateInputDTO();

    public static Clock generateClockInstance(Instant instant, ZoneId zoneId) {

        return Clock.fixed(instant, zoneId);
    }

    public static Validator generateValidatorEngine() {

        return Validation.buildDefaultValidatorFactory().getValidator();
    }
}
