package com.validation.app_user;

import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class AppUserValidator extends BaseDTOValidator {

    public AppUserValidator(Validator validatorEngine) {
        super(validatorEngine);
    }
}
