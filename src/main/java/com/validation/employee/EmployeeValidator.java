package com.validation.employee;

import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator extends BaseDTOValidator {

    public EmployeeValidator(Validator validatorEngine) {
        super(validatorEngine);
    }
}