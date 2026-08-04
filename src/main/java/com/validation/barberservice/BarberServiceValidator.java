package com.validation.barberservice;

import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class BarberServiceValidator extends BaseDTOValidator {

    public BarberServiceValidator(Validator validatorEngine) {
        super(validatorEngine);
    }
}
