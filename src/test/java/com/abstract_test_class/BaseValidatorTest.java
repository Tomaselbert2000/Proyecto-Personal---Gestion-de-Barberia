package com.abstract_test_class;

import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.validation.common.CommonValidationFunctions.generateValidatorEngine;

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
}
